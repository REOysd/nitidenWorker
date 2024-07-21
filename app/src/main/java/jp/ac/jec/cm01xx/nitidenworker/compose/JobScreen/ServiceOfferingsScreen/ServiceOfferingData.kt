package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen

import android.net.Uri

data class ServiceOfferingData(
    val category: String,
    val title:String,
    val subTitle:String,
    val description:String,
    val deliveryDays:String,
    val precautions:String?,
    val selectImages:List<Uri?>,
    val selectMovies:List<Uri?>,
    val checkBoxState:Boolean
)
