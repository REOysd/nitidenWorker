package jp.ac.jec.cm01xx.nitidenworker.compose.FirebaseViewModel

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import jp.ac.jec.cm01xx.nitidenworker.DataModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserDataRepository(
    private val auth:FirebaseAuth,
    private val fireStore:FirebaseFirestore,
    private val fireStorage:FirebaseStorage
) {


    fun startLeadingUserData(userId: String): Flow<DataModel?> = callbackFlow {
        val listenerRegistration = fireStore
            .collection("Users")
            .document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    trySend(snapshot.toObject(DataModel::class.java))
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    suspend fun updateOnMyProfile(key:String,value:Any?) {
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

    suspend fun updateUrlOnMyProfile(urls:List<String>){
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

    suspend fun updateOnOtherProfile(key: String,value: Any?,uid:String){
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