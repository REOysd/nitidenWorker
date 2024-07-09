package jp.ac.jec.cm01xx.nitidenworker.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun HomeScreen(
    modifier: Modifier
){
    Box(
        modifier = modifier
            .fillMaxSize()
    ){
        Icon(painter = painterResource(id = R.drawable.home_icon_by_icons8), contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen(){
}