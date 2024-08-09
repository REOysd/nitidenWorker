package jp.ac.jec.cm01xx.nitidenworker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen.ServiceOfferingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.URL
import java.security.MessageDigest
import java.util.UUID

class FirebaseViewModel:ViewModel() {
    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    val fireStore = FirebaseFirestore.getInstance()

    private val _userData = MutableStateFlow<UserDocument?>(null)
    val userData:StateFlow<UserDocument?> = _userData.asStateFlow()

    private val _myServiceOfferings = MutableStateFlow<List<publishData?>>(emptyList())
    val myServiceOfferings = _myServiceOfferings.asStateFlow()

    private val _myFavoriteServiceOfferings = MutableStateFlow<List<publishData?>>(emptyList())
    val myFavoriteServiceOfferings = _myFavoriteServiceOfferings.asStateFlow()

    private val _serviceOfferings = MutableStateFlow<List<publishData?>>(emptyList())
    val serviceOfferings = _serviceOfferings.asStateFlow()

    private val _serviceOfferingData = MutableStateFlow<publishData?>(null)
    val serviceOfferingData = _serviceOfferingData.asStateFlow()

    private var listenerRegistration: ListenerRegistration? = null

    fun startLeadingUserData(userId:String){
        try{
            listenerRegistration = fireStore
                .collection("Users")
                .document(userId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        _userData.value = snapshot.toObject(UserDocument::class.java)
                    }
                }
        }catch (e:Exception){
            Log.d("startLeadingUserDataError",e.message.toString())
        }
    }

    fun updateOnMyProfile(key:String,value:Any?){
        viewModelScope.launch {
            try{
                auth.currentUser?.let {
                    fireStore
                        .collection("Users")
                        .document(it.uid)
                        .update(key,value)
                        .await()
                }
            }catch (e:Exception){
                Log.d("updateOnMyProfileError",e.message.toString())
            }
        }
    }

    fun updateOnOtherProfile(key: String,value: Any?,uid:String){
        viewModelScope.launch {
            try {
                auth.currentUser?.let {
                    fireStore
                        .collection("Users")
                        .document(uid)
                        .update(key,value)
                        .await()
                }
            } catch (e:Exception){
                Log.d("updateUonOtherProfileError",e.message.toString())
            }
        }
    }

    fun updateUrlOnMyProfile(urls:List<String>){
        viewModelScope.launch {
            try {
                auth.currentUser?.let {
                    fireStore
                        .collection("Users")
                        .document(it.uid)
                        .update("urls",urls)
                        .await()
                }
            }catch (e:Exception){
                Log.d("updateUrlOnMyProfileError",e.message.toString())
            }
        }
    }


    private suspend fun uploadFiles(
        uris:List<Uri?>,
        folder:String,
        context: Context
    ):List<String?>{
        return withContext(Dispatchers.IO){
            uris.filterNotNull().map { file ->
                val reference = Firebase.storage.reference.child("$folder/${UUID.randomUUID()}")

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

                val reference = Firebase.storage.reference.child("UserPhoto/$photoHash")
                reference.putBytes(photoByte).await()
                reference.downloadUrl.await().toString()
            } catch (e: Exception) {
                Log.d("uploadUserPhotoError", e.message.toString())
                ""
            }
        }
    }

    private fun calculateMD5(byte:ByteArray):String{
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(byte)
        return digest.joinToString("") { "%02x".format(it) }
    }

    private suspend fun checkExitingPhoto(photoByte:String):String?{
        return try{
            val reference = Firebase.storage.reference.child("UserPhoto/${photoByte}")
            reference.downloadUrl.await().toString()
        }catch (e:Exception){
            Log.d("checkExitingPhotoError",e.message.toString())
            null
        }
    }

    private suspend fun compressImage(uri:Uri,context:Context):ByteArray{
        return withContext(Dispatchers.IO){
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,outputStream)
            outputStream.toByteArray()
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

    private suspend fun uploadMovieThumbnail(thumbnail:Bitmap?):String?{
        return withContext(Dispatchers.IO){
            thumbnail?.let {
                try{
                    val baos = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                    val thumbnailByteArray = baos.toByteArray()
                    val reference = Firebase.storage.reference.child("movieThumbnail/${UUID.randomUUID()}.jpg")
                    reference.putBytes(thumbnailByteArray).await()

                    reference.downloadUrl.await().toString()
                }catch (e:Exception){
                    Log.d("uploadMovieThumbnailError",e.message.toString())
                    null
                }
            }
        }
    }

    fun publishServiceOfferings(
        serviceOfferingData: ServiceOfferingData,
        context: Context
    ){
        viewModelScope.launch{
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
    }

    fun getServiceOfferings(){
        viewModelScope.launch {
            try{
                val querySnapshot = fireStore
                    .collection("ServiceOfferings")
                    .get()
                    .await()

                _serviceOfferings.value = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(publishData::class.java)
                }

            }catch (e:Exception){
                Log.d("getServiceOfferingsError", e.message.toString())
            }
        }
    }

    fun getMyServiceOfferings(){
        viewModelScope.launch {
            try{
                auth.currentUser?.let { user ->
                    val querySnapshot = fireStore
                        .collection("ServiceOfferings")
                        .whereEqualTo("thisUid",user.uid)
                        .get()
                        .await()

                    _myServiceOfferings.value = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(publishData::class.java)
                    }
                }
            }catch (e:Exception){
                Log.d("getMyServiceOfferingsError", e.toString())
            }
        }
    }

    fun getMyFavoriteServiceOfferings(){
        viewModelScope.launch {
            try {
                auth.currentUser?.let { user ->
                    val querySnapshot = fireStore
                        .collection("ServiceOfferings")
                        .whereArrayContains("favoriteUserIds",user.uid)
                        .get()
                        .await()

                    _myFavoriteServiceOfferings.value = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(publishData::class.java)
                    }
                }

                Log.d("getMyFavoriteServiceOfferings",_myFavoriteServiceOfferings.value.toString())
            } catch (e:Exception){
                Log.d("getMyFavoriteServiceOfferingsError",e.message.toString())
            }
        }
    }

    fun updateServiceOffering(key:String,value:Any?,id:String){
        viewModelScope.launch {
            try{
                auth.currentUser?.let {
                    fireStore
                        .collection("ServiceOfferings")
                        .document(id)
                        .update(key,value)
                        .await()

                    getServiceOfferings()
                    Log.d("updateServiceOffering",value.toString())
                }
            }catch (e:Exception){
                Log.d("updateOnMyProfileError",e.message.toString())
            }
        }
    }

    fun updateListType(id:String?,listType:String){
        if (auth.currentUser == null || id == null) {
            Log.d("updateLikedUsersError", "UID or ID is null")
            return
        }

        viewModelScope.launch{
            try {
                auth.currentUser?.let { currentUser ->
                    val Ref = fireStore.collection("ServiceOfferings").document(id)

                    fireStore.runTransaction { transaction ->
                        val snapshot = transaction.get(Ref)
                        val thisUid = snapshot.get("thisUid").toString()
                        val currentList =
                            snapshot.get(listType) as? List<String> ?: emptyList()

                        if (currentUser.uid in currentList) {
                            transaction.update(Ref, listType, FieldValue.arrayRemove(currentUser.uid))

                            if(listType == "likedUserIds"){
                                updateOnOtherProfile("totalLikes", FieldValue.increment(-1), thisUid)
                            }
                        } else {
                            transaction.update(Ref, listType, FieldValue.arrayUnion(currentUser.uid))

                            if(listType == "likedUserIds"){
                                updateOnOtherProfile("totalLikes", FieldValue.increment(1), thisUid)
                            }
                        }
                    }.await()
                }
            } catch (e: Exception) {
                Log.d("updateLikedUsersError", e.toString())
            }

            getServiceOfferings()
        }
    }

    fun getServiceOfferingData(id:String) {
        viewModelScope.launch {
            try{
                auth.currentUser?.let {
                    val querySnapshot = fireStore
                        .collection("ServiceOfferings")
                        .whereEqualTo("id", id)
                        .get()
                        .await()

                    querySnapshot.documents.firstOrNull()?.let {
                         _serviceOfferingData.value= it.toObject(publishData::class.java)
                    }
                }
            }catch (e:Exception){
                Log.d("getServiceOfferingDataError",e.message.toString())
            }
        }
    }

    fun cleanServiceOfferingData(
    ){
        _serviceOfferingData.value = null
    }
}

data class publishData(
    val id:String = "",
    val thisUid:String = "",
    val email:String = "",
    val name:String = "",
    val displayName:String = "",
    val job:String = "",
    val totalLikes:String = "",
    val numberOfAchievement:String = "--",
    val completionRate:String = "--",
    val photoUrl:String? = "",
    val category: String = "",
    val title:String = "",
    val subTitle:String = "",
    val description:String = "",
    val deliveryDays:String = "",
    val precautions:String? = null,
    val selectImages:List<String?> = emptyList(),
    val selectMovies:List<String?> = emptyList(),
    val selectImageThumbnail:String? = null,
    val checkBoxState:Boolean = false,
    val niceCount:Int = 0,
    val likedUserIds:List<String> = listOf(),
    val favoriteUserIds:List<String> = listOf(),
    val favoriteCount:Int = 0,
    val applyingCount:Int = 0,
    val timestamp: Timestamp = Timestamp.now(),
)