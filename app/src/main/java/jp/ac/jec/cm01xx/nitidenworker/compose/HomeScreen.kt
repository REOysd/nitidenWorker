package jp.ac.jec.cm01xx.nitidenworker.compose

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
fun HomeScreen(
    modifier: Modifier,
    getServiceOfferings:() -> Unit,
    serviceOfferings_:StateFlow<List<publishData?>>,
    onClickToServiceOfferingsDetailScreen:(String) -> Unit
){
    val serviceOfferings = serviceOfferings_.collectAsState()

    LaunchedEffect(Unit) {
        getServiceOfferings()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        items(items = serviceOfferings.value){
            Spacer(modifier = Modifier.height(6.dp))

            it?.let{
                ServiceOfferingsViewingScreen(
                    serviceOfferings = it,
                    isAuthDataVisible = true,
                    onClickToServiceOfferingsDetailScreen =
                    { onClickToServiceOfferingsDetailScreen(it.id) }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

        }
    }
}