package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm01xx.nitidenworker.compose.ServiceOfferingsViewingScreen.ServiceOfferingsViewingScreen
import jp.ac.jec.cm01xx.nitidenworker.PublishData
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RequestServiceScreen(
    uid:String?,
    modifier: Modifier,
    myServiceOfferings:StateFlow<List<PublishData?>>,
    getMyServiceOfferings:() -> Unit,
    getServiceOfferingData:(String) -> Unit,
    cleanServiceOfferingCreationPreview:() -> Unit,
    updateLikedUsers:(String,String) -> Unit,
    updateFavoriteUsers:(String,String) -> Unit,
    onClickHeartAndFavoriteIcon:(String,Boolean,String) -> Unit,
    onClickToServiceOfferingDetailScreen:() -> Unit,

    ){
    val serviceOffering = myServiceOfferings.collectAsState()

    LaunchedEffect(Unit) {
        getMyServiceOfferings()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        items(items = serviceOffering.value){ item ->
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
                        cleanServiceOfferingCreationPreview()
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
                    onClickHeartIcon = {
                        onClickHeartAndFavoriteIcon(
                            "niceCount",
                            it,
                            serviceOffering.id
                        )
                    },
                    onClickFavoriteIcon = {
                        onClickHeartAndFavoriteIcon(
                            "favoriteCount",
                            it,
                            serviceOffering.id
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}