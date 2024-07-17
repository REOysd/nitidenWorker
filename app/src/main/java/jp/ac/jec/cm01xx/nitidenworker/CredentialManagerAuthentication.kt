package jp.ac.jec.cm01xx.nitidenworker

import android.content.Context
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.navigation.NavHostController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

fun CredentialManagerAuthentication(
    firebaseViewModel: FirebaseViewModel,
    navHostController: NavHostController,
    context:Context,
    scope:CoroutineScope,
    credentialManager: CredentialManager
){
    val WEB_CLIENT_ID = "899480932485-vq9dkp81a41l1kargodov0ld004sndsi.apps.googleusercontent.com"

    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(WEB_CLIENT_ID)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    scope.launch {
        try {
            val result = credentialManager.getCredential(
                request = request,
                context = context
            )
            val credential = result.credential
            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credential.data)
            val googleIdToken = googleIdTokenCredential.idToken
            val firebaseCredential = GoogleAuthProvider.getCredential(
                googleIdToken,
                null
            )

            val authResult =
                firebaseViewModel.auth.signInWithCredential(firebaseCredential).await()
            val user = authResult.user

            if (user != null) {
                val userDocument = FirebaseFirestore.getInstance()
                    .collection("Users").document(user.uid)

                if (!userDocument.get().await().exists()) {
                    val newUser = user.let {
                        UserDocument(
                            uid = it.uid,
                            mail = it.email.toString(),
                            name = it.displayName.toString(),
                        )
                    }
                    userDocument.set(newUser).await()
                }
                val userDocumentUpdate = userDocument.get().await()
                val userData = userDocumentUpdate.toObject(UserDocument::class.java)

                if (userData != null) {
                    println(userData)
                }

                navHostController.navigate(NavigationScreen.User.name)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                context,
                "${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}