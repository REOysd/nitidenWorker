package jp.ac.jec.cm01xx.nitidenworker.compose.ServiceOfferingsViewingScreen

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import jp.ac.jec.cm01xx.nitidenworker.publishData
import java.text.SimpleDateFormat
import java.util.Locale

class ServiceOfferingsViewingViewModel(serviceOfferings: publishData?): ViewModel() {
    val selectImages = serviceOfferings?.selectImages
    val selectMovies = serviceOfferings?.selectMovies

    fun formatTimeStamp(timeStamp: Timestamp):String{
        val data = timeStamp.toDate()
        val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return formatter.format(data)
    }


}