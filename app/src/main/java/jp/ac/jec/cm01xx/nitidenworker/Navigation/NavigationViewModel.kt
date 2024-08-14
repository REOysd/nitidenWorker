package jp.ac.jec.cm01xx.nitidenworker.Navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.lifecycle.ViewModel
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.ServiceOfferingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel:ViewModel() {
    private val _serviceOfferingData = MutableStateFlow<ServiceOfferingData?>(null)
    val serviceOfferingData:StateFlow<ServiceOfferingData?> = _serviceOfferingData

    private val _isBottomBarVisible = MutableStateFlow(true)
    val isBottomBarVisible:StateFlow<Boolean> = _isBottomBarVisible

    private val _selectedItemIndex = MutableStateFlow(0)
    val selectedItemIndex:StateFlow<Int> = _selectedItemIndex

    private var lastScrollOffset = 0f

    val navigationItems = listOf(
        NavigationItem.Home,
        NavigationItem.Search,
        NavigationItem.Favorite,
        NavigationItem.Message,
        NavigationItem.MyJob
    )
    val nestScrollConnection = object:NestedScrollConnection{
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y
            if(delta < 0){
                setBottomBarVisible(false)
            }else if(delta > 0){
                setBottomBarVisible(true)
            }
            lastScrollOffset += delta
            return Offset.Zero
        }
    }

    fun setServiceOfferingData(data:ServiceOfferingData?){
        _serviceOfferingData.value = data
    }

    fun setSelectedItemIndex(selectedIndex:Int){
        _selectedItemIndex.value = selectedIndex
    }

    fun setBottomBarVisible(isVisible:Boolean){
        _isBottomBarVisible.value = isVisible
    }

}

sealed class NavigationItem(
    val route:String,
    val title:String,
    val selectedIcon:Int,
    val unSelectedIcon:Int
){
    object Home:NavigationItem(
        route = NavigationScreen.Home.name,
        title = "Home",
        selectedIcon = R.drawable.home_icon_by_icons8,
        unSelectedIcon = R.drawable.home_icon_black_by_icons8
    )
    object Search:NavigationItem(
        route = NavigationScreen.Search.name,
        title = "Search",
        selectedIcon = R.drawable.search_icon_by_icons8,
        unSelectedIcon = R.drawable.search_icon_black_by_icons8,
    )
    object Favorite:NavigationItem(
        route = NavigationScreen.Favorite.name,
        title = "Favorite",
        selectedIcon = R.drawable.favorite_icon_by_icons8,
        unSelectedIcon = R.drawable.favorite_icon_black_by_icons8
    )
    object Message:NavigationItem(
        route = NavigationScreen.Message.name,
        title = "Message",
        selectedIcon = R.drawable.message_icon_by_icons8,
        unSelectedIcon = R.drawable.message_icon_black_by_icons8
    )
    object MyJob:NavigationItem(
        route = NavigationScreen.MyJob.name,
        title = "MyJob",
        selectedIcon = R.drawable.job_icon_by_icons8,
        unSelectedIcon = R.drawable.job_icon_black_by_icons8
    )
}