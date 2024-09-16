package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ac.jec.cm01xx.nitidenworker.PublishData
import jp.ac.jec.cm01xx.nitidenworker.UserDocument
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServiceOfferingsDetailViewingViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(ServiceOfferingsDetailViewingUiState())
    val uiState: StateFlow<ServiceOfferingsDetailViewingUiState> = _uiState.asStateFlow()

    fun initializeData(
        userDocument:UserDocument?,
        serviceOfferingData:PublishData?,
        isShowConfirmDialog:Boolean = false,
        isShowSelectedImageAndMovieDialog:Boolean = false
        ) {
        val _selectImageAndMovie =  serviceOfferingData?.let {
            it.selectImages + it.selectMovies
        }?: emptyList()

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    userDocument = userDocument,
                    serviceOfferingData = serviceOfferingData,
                    selectedImageAndMovie = _selectImageAndMovie,
                    isShowConfirmDialog = isShowConfirmDialog,
                    isShowSelectedImageAndMovieDialog = isShowSelectedImageAndMovieDialog,
                    isInitialized = true
                )
            }
        }
    }

    fun changeIsShowSelectedImageAndMovieDialog(isShowSelectedImageAndMovieDialog:Boolean) {
        _uiState.value = _uiState.value.copy(
            isShowSelectedImageAndMovieDialog = isShowSelectedImageAndMovieDialog
        )
    }

    fun changeIsShowConfirmDialog(isShowConfirmDialog:Boolean) {
        _uiState.value = _uiState.value.copy(
            isShowConfirmDialog = isShowConfirmDialog
        )
    }

    fun changeImageAndMovieIndex(index:Int){
        _uiState.value = uiState.value.copy(
            imageAndMovieIndex = index
        )
    }
}

data class ServiceOfferingsDetailViewingUiState(
    val userDocument:UserDocument? = null,
    val serviceOfferingData:PublishData? = null,
    val selectedImageAndMovie:List<String?> = emptyList(),
    val imageAndMovieIndex:Int = 0,
    val isShowConfirmDialog:Boolean = false,
    val isShowSelectedImageAndMovieDialog:Boolean = false,
    val isInitialized:Boolean = false
)