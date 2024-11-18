package jp.ac.jec.cm01xx.nitidenworker.compose.FirebaseViewModel

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class MessageRepository(
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val fireStorage: FirebaseStorage,
) {
    fun generateConversationId(userId1: String, userId2: String): String {
        return listOf(userId1, userId2).sorted().joinToString("_")
    }

//    suspend fun sendMessage(conversationId:String,messages:Messages) {
//        val conversationRef = fireStore.collection("Conversations").document(conversationId)
//
//        try {
//            fireStore.runTransaction { transaction ->
//                val snapshot = transaction.get(conversationRef)
//
//                if(!snapshot.exists()) {
//                    val newConversation = Messages(
//                        documentId = conversationId,
//                         messageContent = listOf(messages.messageContent)
//                    )
//                    transaction.set(conversationRef, messages)
//                }else {
//                    val existingContent = snapshot.get("MessageContent") as? List<MessageDetail> ?: listOf()
//                    transaction.update(
//                        conversationRef,
//                        "MessageContent",
//                        existingContent + messages.messageContent
//                    )
//                }
//
//                Log.d("sendMessageError",snapshot.toString())
//            }.await()
//        }catch (e:Exception) {
//            Log.e("sendMessageError",e.message.toString())
//        }
//    }
}

data class Messages(
    val documentId:String,
    val messageContent:MessageDetail,
)

data class MessageDetail(
    val senderId:String,
    val messageText:String,
    val timestamp: Timestamp = Timestamp.now(),
)