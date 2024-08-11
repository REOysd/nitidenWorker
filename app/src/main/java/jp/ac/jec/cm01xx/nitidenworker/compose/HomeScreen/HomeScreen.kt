package jp.ac.jec.cm01xx.nitidenworker.compose.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm01xx.nitidenworker.compose.ServiceOfferingsViewingScreen.ServiceOfferingsViewingScreen
import jp.ac.jec.cm01xx.nitidenworker.publishData
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    uid:String?,
    modifier: Modifier,
    getServiceOfferings:() -> Unit,
    serviceOfferings_:StateFlow<List<publishData?>>,
    getServiceOfferingData:(String) -> Unit,
    onClickToServiceOfferingDetailScreen:() -> Unit,
    onClickTOProfile:() -> Unit,
    updateLikedUsers:(String,String) -> Unit,
    updateFavoriteUsers:(String,String) -> Unit,
    onClickHeartAndFavoriteIcon:(String,Int,String) -> Unit,
    cleanServiceOfferingData:() -> Unit,
){
    val serviceOfferings = serviceOfferings_.collectAsState()

    LaunchedEffect(Unit) {
        getServiceOfferings()
    }

    Scaffold(
        topBar = {
            HomeScreenTopBar(
                onClickToProfile = onClickTOProfile,
                cleanServiceOfferingData = cleanServiceOfferingData,
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
            )
        }
    )
    { innerPadding ->
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            items(items = serviceOfferings.value) {item ->
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

                Spacer(modifier = Modifier.height(6.dp))

            }
        }
    }
}