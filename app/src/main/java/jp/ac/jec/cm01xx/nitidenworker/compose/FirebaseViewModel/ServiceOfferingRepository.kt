package jp.ac.jec.cm01xx.nitidenworker.compose.FirebaseViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import jp.ac.jec.cm01xx.nitidenworker.ServiceOfferingData
import jp.ac.jec.cm01xx.nitidenworker.publishData
import jp.ac.jec.cm01xx.nitidenworker.userDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.URL
import java.security.MessageDigest
import java.util.UUID

class ServiceOfferingRepository(
    private val auth:FirebaseAuth,
    private val fireStore:FirebaseFirestore,
    private val fireStorage:FirebaseStorage,
) {

    suspend fun publishServiceOfferings(
        serviceOfferingData: ServiceOfferingData,
        context: Context,
        userData:StateFlow<userDocument?>
    ){
        try{
            val id:String = UUID.randomUUID().toString()
            val images = uploadFiles(serviceOfferingData.selectImages, "images",context)
            val authPhotoUrl = uploadUserPhoto(auth.currentUser?.photoUrl)
            val movies = uploadFiles(serviceOfferingData.selectMovies, "movies",context)
            var movieThumbnail:String? = null

            if(movies.isNotEmpty()){
                val _movieThumbnail = createThumbnail(movies.firstOrNull())
                movieThumbnail = uploadMovieThumbnail(_movieThumbnail)
            }

            val publishData = publishData(
                id = id,
                thisUid = auth.currentUser?.uid.toString(),
                email = auth.currentUser?.email.toString(),
                name = userData.value?.name.toString(),
                displayName = auth.currentUser?.displayName.toString(),
                job = userData.value?.job.toString(),
                totalLikes = userData.value?.completionRate.toString(),
                numberOfAchievement = userData.value?.numberOfAchievement.toString(),
                completionRate = userData.value?.completionRate.toString(),
                photoUrl = authPhotoUrl,
                category = serviceOfferingData.category,
                title = serviceOfferingData.title,
                subTitle = serviceOfferingData.subTitle,
                description = serviceOfferingData.description,
                deliveryDays = serviceOfferingData.deliveryDays,
                precautions = serviceOfferingData.precautions,
                selectImages = images,
                selectMovies = movies,
                selectImageThumbnail = movieThumbnail,
                checkBoxState = serviceOfferingData.checkBoxState,
                niceCount = serviceOfferingData.niceCount,
                favoriteCount = serviceOfferingData.favoriteCount,
                applyingCount = serviceOfferingData.applyingCount,
            )

            auth.currentUser?.let {
                fireStore
                    .collection("ServiceOfferings")
                    .document(id)
                    .set(publishData)
                    .await()
            }

        }catch (e:Exception){
            Log.d("publishServiceOfferingsError",e.message.toString())
        }
    }

    suspend fun getServiceOfferings():List<publishData>{
        return try {
            val querySnapshot = fireStore
                .collection("ServiceOfferings")
                .get()
                .await()

            querySnapshot.documents.mapNotNull { document ->
                document.toObject(publishData::class.java)
            }

        } catch (e:Exception) {
            Log.d("getServiceOfferingsError", e.message.toString())
            emptyList()
        }
    }

    suspend fun updateServiceOffering(key:String,value:Any?,id:String){
        try {
            auth.currentUser?.let {
                fireStore
                    .collection("ServiceOfferings")
                    .document(id)
                    .update(key,value)
                    .await()

                getServiceOfferings()
            }
        } catch (e:Exception) {
            Log.d("updateOnMyProfileError",e.message.toString())
        }
    }

    suspend fun updateListTypeOfServiceOffering(
        id: String?,
        listType: String,
        ) {
        if (auth.currentUser == null || id == null) {
            Log.d("updateListTypeError", "UID or ID is null")
            return
        }

        try {
            auth.currentUser?.let { currentUser ->
                val ref = fireStore.collection("ServiceOfferings").document(id)

                try {
                    fireStore.runTransaction { transaction ->
                        val snapshot = transaction.get(ref)
                        val currentList = snapshot.get(listType) as? List<String> ?: emptyList()

                        if (currentUser.uid in currentList) {
                            transaction.update(ref, listType, FieldValue.arrayRemove(currentUser.uid))
                        } else {
                            transaction.update(ref, listType, FieldValue.arrayUnion(currentUser.uid))
                        }
                    }.await()

                } catch (e: FirebaseFirestoreException) {
                    if (e.code == FirebaseFirestoreException.Code.ABORTED) {
                        Log.d("updateListType", e.message.toString())
                    } else {
                        throw e
                    }
                }

            }
        } catch (e: Exception) {
            Log.e("updateListTypeError", "Error updating $listType: ${e.message}")
        }

        getServiceOfferings()
    }

    suspend fun getMyServiceOfferings():List<publishData>{
        return try{
            auth.currentUser?.let { user ->
                val querySnapshot = fireStore
                    .collection("ServiceOfferings")
                    .whereEqualTo("thisUid",user.uid)
                    .get()
                    .await()

                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(publishData::class.java)
                }
            }?: emptyList()
        }catch (e:Exception){
            Log.d("getMyServiceOfferingsError", e.toString())
            emptyList()
        }
    }

    suspend fun getMyFavoriteServiceOfferings():List<publishData>{
        return try {
            auth.currentUser?.let { user ->
                val querySnapshot = fireStore
                    .collection("ServiceOfferings")
                    .whereArrayContains("favoriteUserIds",user.uid)
                    .get()
                    .await()

                querySnapshot.documents.mapNotNull { document ->
                    document.toObject(publishData::class.java)
                }
            }?: emptyList()
        } catch (e:Exception){
            Log.d("getMyFavoriteServiceOfferingsError",e.message.toString())
            emptyList()
        }
    }

    suspend fun getServiceOfferingData(id:String):publishData? {
        return try{
            auth.currentUser?.let {
                val querySnapshot = fireStore
                    .collection("ServiceOfferings")
                    .whereEqualTo("id", id)
                    .get()
                    .await()

                querySnapshot.documents.firstOrNull()?.let {
                    it.toObject(publishData::class.java)
                }
            }
        }catch (e:Exception){
            Log.d("getServiceOfferingDataError",e.message.toString())
            null
        }
    }

    fun cleanServiceOfferingData():publishData?{
        return null
    }

    suspend fun uploadFiles(
        uris:List<Uri?>,
        folder:String,
        context: Context
    ):List<String?>{
        return withContext(Dispatchers.IO){
            uris.filterNotNull().map { file ->
                val reference = fireStorage.reference.child("$folder/${UUID.randomUUID()}")

                if(folder == "images"){
                    val compressedBytes = compressImage(file,context)

                    reference.putBytes(compressedBytes).await()
                }else{
                    reference.putFile(file).await()
                }

                reference.downloadUrl.await().toString()
            }
        }
    }

    suspend fun compressImage(uri: Uri, context: Context):ByteArray{
        return withContext(Dispatchers.IO){
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream)
            outputStream.toByteArray()
        }
    }

    suspend fun uploadUserPhoto(photoUrl:Uri?):String{
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = URL(photoUrl.toString()).openStream()
                val photoByte = inputStream.readBytes()
                val photoHash = calculateMD5(photoByte)
                val exitingPhotoUrl = checkExitingPhoto(photoHash)

                exitingPhotoUrl?.let {
                    return@withContext it
                }

                val reference = fireStorage.reference.child("UserPhoto/$photoHash")
                reference.putBytes(photoByte).await()
                reference.downloadUrl.await().toString()
            } catch (e: Exception) {
                Log.d("uploadUserPhotoError", e.message.toString())
                ""
            }
        }
    }

    fun calculateMD5(byte:ByteArray):String{
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(byte)
        return digest.joinToString("") { "%02x".format(it) }
    }

    suspend fun checkExitingPhoto(photoByte:String):String?{
        return try{
            val reference = fireStorage.reference.child("UserPhoto/${photoByte}")
            reference.downloadUrl.await().toString()
        }catch (e:Exception){
            Log.d("checkExitingPhotoError",e.message.toString())
            null
        }
    }

    suspend fun createThumbnail(videoUri:String?):Bitmap?{
        return withContext(Dispatchers.IO) {
            videoUri.let { movieUri ->
                try {
                    val retriever = MediaMetadataRetriever()
                    retriever.setDataSource(
                        movieUri,
                        HashMap<String, String>()
                    )
                    val thumbnail = retriever.frameAtTime
                    retriever.release()
                    thumbnail
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }

    suspend fun uploadMovieThumbnail(thumbnail:Bitmap?):String?{
        return withContext(Dispatchers.IO){
            thumbnail?.let {
                try{
                    val baos = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                    val thumbnailByteArray = baos.toByteArray()
                    val reference = fireStorage.reference.child("movieThumbnail/${UUID.randomUUID()}.jpg")
                    reference.putBytes(thumbnailByteArray).await()

                    reference.downloadUrl.await().toString()
                }catch (e:Exception){
                    Log.d("uploadMovieThumbnailError",e.message.toString())
                    null
                }
            }
        }
    }
}