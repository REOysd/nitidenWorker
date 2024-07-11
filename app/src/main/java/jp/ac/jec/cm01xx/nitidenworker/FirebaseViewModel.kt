package jp.ac.jec.cm01xx.nitidenworker

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class FirebaseViewModel:ViewModel() {
    val auth:FirebaseAuth = FirebaseAuth.getInstance()
}