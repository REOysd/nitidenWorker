package jp.ac.jec.cm01xx.nitidenworker

import com.google.firebase.Timestamp

data class UserDocument(
    val uid:String = "",
    val mail:String = "",
    val name:String = "",
    val numberOfAchievement:String = "--",
    val completionRate:String = "--",
    val job:String = "--",
    val selfPresentation:String = "",
    val urls:List<String> = emptyList(),
    val timeStamp: Timestamp = Timestamp.now()
)

