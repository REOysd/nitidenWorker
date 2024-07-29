package jp.ac.jec.cm01xx.nitidenworker.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import jp.ac.jec.cm01xx.nitidenworker.compose.ServiceOfferingsViewingScreen.ServiceOfferingsViewingScreen
import jp.ac.jec.cm01xx.nitidenworker.publishData
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreen(
    modifier: Modifier,
    getServiceOfferings:() -> Unit,
    serviceOfferings:StateFlow<List<publishData?>>
){
    val serviceOfferings = serviceOfferings.collectAsState()

    LaunchedEffect(Unit) {
        getServiceOfferings()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(items = serviceOfferings.value){
            ServiceOfferingsViewingScreen(
                serviceOfferings = it,
                isAuthDataVisible = true
            )
        }
    }
}