package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import jp.ac.jec.cm01xx.nitidenworker.PublishData
import jp.ac.jec.cm01xx.nitidenworker.UserDocument
import kotlinx.coroutines.flow.MutableStateFlow

class ServiceOfferingsDetailViewingViewModel:ViewModel() {
    private val uiState = MutableStateFlow(ServiceOfferingsDetailUiState())

}

data class ServiceOfferingsDetailViewingUiState(
    val userDocument:UserDocument? = null,
    val serviceOfferingData:PublishData? = null
)