package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen.ServiceOfferingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ServiceOfferingsDetailViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(ServiceOfferingsDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectImageAndMovie = MutableStateFlow<List<Uri>>(emptyList())
    val selectImageAndMovie = _selectImageAndMovie.asStateFlow()

    fun initializeData(data:ServiceOfferingData){
        val images = data.selectImages.filterNotNull()
        val movies = data.selectMovies.filterNotNull()

        _selectImageAndMovie.value = images + movies
        _uiState.value = _uiState.value.copy(
            title = data.title,
            subTitle = data.subTitle,
            category = data.category,
            deliveryDays = data.deliveryDays,
            precautions = data.precautions,
            checkBoxState = data.checkBoxState,
            selectImageAndMoviePageCount = images.size + movies.size,
            images = images
        )
    }

    fun onChangedNiceCount(Swith:Boolean){
        if(Swith){
            _uiState.update {it.copy(niceCount = it.niceCount + 1)}
        }else{
            _uiState.update { it.copy(niceCount = it.niceCount - 1) }
        }
    }

    fun onChangedFavoriteCount(Swith:Boolean){
        if(Swith){
            _uiState.update {it.copy(favoriteCount = it.favoriteCount + 1)}
        }else{
            _uiState.update { it.copy(favoriteCount = it.favoriteCount - 1) }
        }
    }
}

data class ServiceOfferingsDetailUiState(
    val title:String = "",
    val subTitle:String = "",
    val category:String = "",
    val deliveryDays:String = "",
    val precautions:String? = "",
    val checkBoxState:Boolean = false,
    val images:List<Uri> = emptyList(),
    val selectImageAndMoviePageCount:Int = 0,
    val niceCount:Int = 0,
    val favoriteCount:Int = 0,
)