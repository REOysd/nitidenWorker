package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm01xx.nitidenworker.FirebaseViewModel
import jp.ac.jec.cm01xx.nitidenworker.compose.ServiceOfferingsViewingScreen.ServiceOfferingsViewingScreen

@Composable
fun RequestServiceScreen(
    modifier: Modifier,
    firebaseViewModel: FirebaseViewModel
){
    val serviceOffering = firebaseViewModel.myServiceOfferings.collectAsState()

    LaunchedEffect(Unit) {
        firebaseViewModel.getMyServiceOfferings()
        Log.d("serviceOffering",serviceOffering.value.toString())
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        items(items = serviceOffering.value){ item ->
            Spacer(modifier = Modifier.height(6.dp))
            ServiceOfferingsViewingScreen(
                serviceOfferings = item,
                isAuthDataVisible = false
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}