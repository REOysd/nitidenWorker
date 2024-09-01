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
import jp.ac.jec.cm01xx.nitidenworker.publishData
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RequestServiceScreen(
    modifier: Modifier,
    myServiceOfferings:StateFlow<List<publishData?>>,
    getMyServiceOfferings:() -> Unit,
    onClickToServiceOfferingsDetailScreen:(String) -> Unit
    ){
    val serviceOffering = myServiceOfferings.collectAsState()

    LaunchedEffect(Unit) {
        getMyServiceOfferings()
        Log.d("serviceOffering",serviceOffering.value.toString())
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        items(items = serviceOffering.value){ item ->
            Spacer(modifier = Modifier.height(6.dp))
            item?.let{
                ServiceOfferingsViewingScreen(
                    uid = item.thisUid,
                    itemUid = item.thisUid,
                    likedUsers = item.likedUserIds,
                    favoriteUsers = item.favoriteUserIds,
                    serviceOfferings = item,
                    isAuthDataVisible = false,
                    onClickToServiceOfferingsDetailScreen =
                    { onClickToServiceOfferingsDetailScreen(item.id) },
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}