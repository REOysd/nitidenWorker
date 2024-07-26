package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import jp.ac.jec.cm01xx.nitidenworker.FirebaseViewModel
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen.ServiceOfferingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ServiceOfferingsDetailViewModel():ViewModel() {
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
            niceCount = data.niceCount,
            favoriteCount = data.favoriteCount,
            applyingCount = data.applyingCount,
            images = images
        )
    }

    fun onChangedNiceCount(
        Swith:Boolean,
        updateTotalNiceCount:(String,Int) -> Unit,
        updateNiceCount:(String,Int,String) -> Unit,
        data: ServiceOfferingData,
    ){
        if(Swith){
            _uiState.update {it.copy(niceCount = it.niceCount + 1)}
            updateTotalNiceCount("totalLikes",data.niceCount + _uiState.value.niceCount)
        }else{
            _uiState.update { it.copy(niceCount = it.niceCount - 1) }
            updateTotalNiceCount("totalLikes",data.niceCount - _uiState.value.niceCount)
        }
        updateNiceCount("niceCount",_uiState.value.niceCount,"ServiceOfferingId")

    }

    fun onChangedFavoriteCount(
        Swith:Boolean,
        updateTotalNiceCount:(String,Int) -> Unit,
        data: ServiceOfferingData,
    ){
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
    val applyingCount:Int = 0
)