package jp.ac.jec.cm01xx.nitidenworker

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen.ServiceOfferingsDetailUiState
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen.ServiceOfferingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirebaseViewModel:ViewModel() {
    val auth:FirebaseAuth = FirebaseAuth.getInstance()
    val fireStore = FirebaseFirestore.getInstance()
    private val _userData = MutableStateFlow<UserDocument?>(null)
    val userData:StateFlow<UserDocument?> = _userData.asStateFlow()

    private var listenerRegistration: ListenerRegistration? = null

    fun startLeadingUserData(userId:String){
        listenerRegistration = fireStore
            .collection("Users")
            .document(userId)
            .addSnapshotListener{snapshot,error ->
                if(error != null){
                    return@addSnapshotListener
                }
                if(snapshot != null && snapshot.exists()){
                    _userData.value = snapshot.toObject(UserDocument::class.java)
                }
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

    fun publishServiceOfferings(
        serviceOfferingData: ServiceOfferingData
    ){
        val images = serviceOfferingData.selectImages.filterNotNull().map { it.toString() }
        val movies = serviceOfferingData.selectMovies.filterNotNull().map { it.toString() }
        val publishData = publishData(
            category = serviceOfferingData.category,
            title = serviceOfferingData.title,
            subTitle = serviceOfferingData.subTitle,
            description = serviceOfferingData.description,
            deliveryDays = serviceOfferingData.deliveryDays,
            precautions = serviceOfferingData.precautions,
            selectImages = images,
            selectMovies = movies,
            checkBoxState = serviceOfferingData.checkBoxState,
            niceCount = serviceOfferingData.niceCount,
            favoriteCount = serviceOfferingData.favoriteCount,
            applyingCount = serviceOfferingData.applyingCount
        )

        viewModelScope.launch {
            try {
                auth.currentUser?.let {
                    fireStore
                        .collection("ServiceOfferings")
                        .add(publishData)
                        .await()
                }
            }catch (e:Exception){
                Log.d("publishServiceOfferingsError",e.message.toString())
            }
        }
    }

    fun updateServiceOfferings(key:String,value:Any?,offeringId:String){
        viewModelScope.launch {
            try{
                auth.currentUser?.let {
                    fireStore
                        .collection("ServiceOfferings")
                        .document(offeringId)
                        .update(key,value)
                        .await()
                }
            }catch (e:Exception){
                Log.d("updateOnMyProfileError",e.message.toString())
            }
        }
    }
}

data class publishData(
    val category: String = "",
    val title:String = "",
    val subTitle:String = "",
    val description:String = "",
    val deliveryDays:String = "",
    val precautions:String? = null,
    val selectImages:List<String?> = emptyList(),
    val selectMovies:List<String?> = emptyList(),
    val checkBoxState:Boolean = false,
    val niceCount:Int = 0,
    val favoriteCount:Int = 0,
    val applyingCount:Int = 0,
    val timestamp: Timestamp = Timestamp.now(),
)