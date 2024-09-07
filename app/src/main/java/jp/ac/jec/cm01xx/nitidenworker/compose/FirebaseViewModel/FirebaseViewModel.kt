package jp.ac.jec.cm01xx.nitidenworker.compose.FirebaseViewModel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import jp.ac.jec.cm01xx.nitidenworker.ServiceOfferingData
import jp.ac.jec.cm01xx.nitidenworker.PublishData
import jp.ac.jec.cm01xx.nitidenworker.UserDocument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseViewModel:ViewModel() {
    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    val fireStore = FirebaseFirestore.getInstance()
    val fireStorage = FirebaseStorage.getInstance()
    val userDataRepository = UserDataRepository(auth, fireStore, fireStorage)
    val serviceOfferingRepository = ServiceOfferingRepository(auth,fireStore, fireStorage)

    private val _userData = MutableStateFlow<UserDocument?>(null)
    val userData:StateFlow<UserDocument?> = _userData.asStateFlow()

    private val _myServiceOfferings = MutableStateFlow<List<PublishData?>>(emptyList())
    val myServiceOfferings = _myServiceOfferings.asStateFlow()

    private val _myFavoriteServiceOfferings = MutableStateFlow<List<PublishData?>>(emptyList())
    val myFavoriteServiceOfferings = _myFavoriteServiceOfferings.asStateFlow()

    private val _serviceOfferings = MutableStateFlow<List<PublishData?>>(emptyList())
    val serviceOfferings = _serviceOfferings.asStateFlow()

    private val _serviceOfferingData = MutableStateFlow<PublishData?>(null)
    val serviceOfferingData = _serviceOfferingData.asStateFlow()


    fun startLeadingUserData(userId:String){
        viewModelScope.launch {
            try {
                userDataRepository.startLeadingUserData(userId).collect { UserDocument ->
                    _userData.value = UserDocument
                }
            } catch (e:Exception){
                Log.d("startLeadingUserDataError", e.message.toString())
            }
        }
    }

    fun updateOnMyProfile(key:String,value:Any?){
        viewModelScope.launch { userDataRepository.updateOnMyProfile(key, value) }
    }

    fun updateOnOtherProfile(key: String,value: Any?,uid:String){
        viewModelScope.launch { userDataRepository.updateOnOtherProfile(key, value, uid) }
    }

    fun updateUrlOnMyProfile(urls:List<String>){
        viewModelScope.launch { userDataRepository.updateUrlOnMyProfile(urls) }
    }

    fun publishServiceOfferings(
        serviceOfferingData: ServiceOfferingData,
        context: Context
    ){
        viewModelScope.launch{
            serviceOfferingRepository.publishServiceOfferings(serviceOfferingData,context,userData)
        }
    }

    fun getServiceOfferings(){
        viewModelScope.launch {
            _serviceOfferings.value = serviceOfferingRepository.getServiceOfferings()
        }
    }

    fun getMyServiceOfferings(){
        viewModelScope.launch {
            _myServiceOfferings.value = serviceOfferingRepository.getMyServiceOfferings()
        }
    }

    fun getMyFavoriteServiceOfferings(){
        viewModelScope.launch {
            _myFavoriteServiceOfferings.value = serviceOfferingRepository.getMyFavoriteServiceOfferings()
        }
    }
    fun updateServiceOffering(key:String,value:Any?,id:String){
        viewModelScope.launch {
            serviceOfferingRepository.updateServiceOffering(key, value, id)
        }
    }

    fun onClickHeartAndFavoriteIcon(key: String, increment: Boolean, id: String){
        try {
            viewModelScope.launch {
                auth.currentUser?.let {
                    val fieldValue = if(increment){
                        FieldValue.increment(1)
                    }else{
                        FieldValue.increment(-1)
                    }

                    val serviceDoc = fireStore
                        .collection("ServiceOfferings")
                        .document(id)
                        .get()
                        .await()

                    val uid = serviceDoc.getString("thisUid")

                    fireStore
                        .collection("ServiceOfferings")
                        .document(id)
                        .update(key, fieldValue)
                        .await()

                    if(key == "niceCount"){
                        uid?.let{
                            updateOnOtherProfile(
                                key = "totalLikes",
                                value = fieldValue,
                                uid = uid
                            )
                        }
                    }

                    getServiceOfferingData(id)
                }
            }
        } catch (e:Exception){
            Log.d("onClickHeartAndFavoriteIconError",e.message.toString())
        }
    }

    fun updateListTypeOfServiceOffering(id:String?,listType:String){
        viewModelScope.launch {
            serviceOfferingRepository.updateListTypeOfServiceOffering(
                id,
                listType,
            )
        }
    }

    fun getServiceOfferingData(id:String) {
        viewModelScope.launch {
            _serviceOfferingData.value = serviceOfferingRepository.getServiceOfferingData(id)
        }
    }

    fun cleanServiceOfferingData(
    ){
        _serviceOfferingData.value = serviceOfferingRepository.cleanServiceOfferingData()
    }

    private suspend fun uploadFiles(
        uris:List<Uri?>,
        folder:String,
        context: Context
    ):List<String?>{
       return serviceOfferingRepository.uploadFiles(uris, folder, context)
    }

    suspend fun uploadUserPhoto(photoUrl:Uri?):String{
        return serviceOfferingRepository.uploadUserPhoto(photoUrl)
    }

    private fun calculateMD5(byte:ByteArray):String{
       return serviceOfferingRepository.calculateMD5(byte)
    }

    suspend fun checkExitingPhoto(photoByte:String):String?{
        return serviceOfferingRepository.checkExitingPhoto(photoByte)
    }

    private suspend fun compressImage(uri:Uri,context:Context):ByteArray{
        return serviceOfferingRepository.compressImage(uri, context)
    }

    suspend fun createThumbnail(videoUri:String?):Bitmap?{
       return serviceOfferingRepository.createThumbnail(videoUri)
    }

    private suspend fun uploadMovieThumbnail(thumbnail:Bitmap?):String?{
        return serviceOfferingRepository.uploadMovieThumbnail(thumbnail)
    }
}
