package jp.ac.jec.cm01xx.nitidenworker.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm01xx.nitidenworker.compose.ServiceOfferingsViewingScreen.ServiceOfferingsViewingScreen
import jp.ac.jec.cm01xx.nitidenworker.publishData
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FavoriteScreen(
    uid:String?,
    getServiceOfferingData:(String) -> Unit,
    getMyFavoriteServiceOfferings:() -> Unit,
    updateLikedUsers:(String,String) -> Unit,
    updateFavoriteUsers:(String,String) -> Unit,
    onClickHeartAndFavoriteIcon:(String,Int,String) -> Unit,
    onClickToServiceOfferingDetailScreen:() -> Unit,
    myFavoriteServiceOfferings:StateFlow<List<publishData?>>,
    modifier: Modifier
){
    val myFavoriteServiceOfferings = myFavoriteServiceOfferings.collectAsState()

    LaunchedEffect(Unit) {
        getMyFavoriteServiceOfferings()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(items = myFavoriteServiceOfferings.value){ item ->
            Spacer(modifier = Modifier.height(6.dp))

            item?.let { serviceOffering ->
                ServiceOfferingsViewingScreen(
                    uid = uid,
                    itemUid = item.thisUid,
                    likedUsers = serviceOffering.likedUserIds,
                    favoriteUsers = serviceOffering.favoriteUserIds,
                    serviceOfferings = serviceOffering,
                    isAuthDataVisible = true,
                    onClickToServiceOfferingsDetailScreen =
                    {
                        getServiceOfferingData(serviceOffering.id)
                        onClickToServiceOfferingDetailScreen()
                    },
                    updateLikedUsers = {
                        updateLikedUsers(
                            serviceOffering.id,
                            "likedUserIds"
                        )
                    },
                    updateFavoriteUsers = {
                        updateFavoriteUsers(
                            serviceOffering.id,
                            "favoriteUserIds"
                        )
                    },
                    onClickHeartIcon =
                    {
                        onClickHeartAndFavoriteIcon(
                            "niceCount",
                            serviceOffering.niceCount + it,
                            serviceOffering.id
                        )
                    },
                    onClickFavoriteIcon = {
                        onClickHeartAndFavoriteIcon(
                            "favoriteCount",
                            serviceOffering.favoriteCount + it,
                            serviceOffering.id
                        )
                    }
                )
            }
        }
    }
    }