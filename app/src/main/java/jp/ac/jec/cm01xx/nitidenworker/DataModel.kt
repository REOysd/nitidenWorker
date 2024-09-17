package jp.ac.jec.cm01xx.nitidenworker

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class UserDocument(
    val uid:String = "",
    val mail:String = "",
    val name:String = "",
    val userPhoto:String = "",
    val numberOfAchievement:String = "--",
    val completionRate:String = "--",
    val job:String = "--",
    val selfPresentation:String = "",
    val urls:List<String> = emptyList(),
    val totalLikes:Int = 0,
    @ServerTimestamp
    val timeStamp: Timestamp = Timestamp.now()
)

data class PublishData(
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
    val applicant:List<String?> = listOf(),
    val timestamp: Timestamp = Timestamp.now(),
)

data class ServiceOfferingData(
    val category: String,
    val title:String,
    val subTitle:String,
    val description:String,
    val deliveryDays:String,
    val precautions:String?,
    val selectImages:List<Uri?>,
    val selectMovies:List<Uri?>,
    val checkBoxState:Boolean,
    val niceCount:Int,
    val favoriteCount:Int,
    val applyingCount:Int
)

