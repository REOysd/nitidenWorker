package jp.ac.jec.cm01xx.nitidenworker.newCompose.Home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm01xx.nitidenworker.newCompose.common.HomeTopBar

@Composable
fun Home() {
    Scaffold(
        topBar = {
            HomeTopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
            )
        }
    ){innerPadding ->
        LoadingHomeScreen()

    }
}


@Composable
fun LoadingHomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        CircularProgressIndicator(
            color = Color.Gray,
            strokeWidth = 5.dp,
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Home()
}