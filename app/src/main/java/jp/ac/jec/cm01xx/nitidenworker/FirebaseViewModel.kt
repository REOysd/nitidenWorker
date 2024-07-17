package jp.ac.jec.cm01xx.nitidenworker

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
                    viewModelScope.launch {
                        fireStore
                            .collection("Users")
                            .document(it.uid)
                            .update(key,value)
                            .await()
                    }
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
                    viewModelScope.launch {
                        fireStore
                            .collection("Users")
                            .document(it.uid)
                            .update("urls",urls)
                            .await()
                    }
                }
            }catch (e:Exception){
                Log.d("updateUrlOnMyProfileError",e.message.toString())
            }
        }
    }

}