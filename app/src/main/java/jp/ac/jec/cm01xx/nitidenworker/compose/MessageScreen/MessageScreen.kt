package jp.ac.jec.cm01xx.nitidenworker.compose.MessageScreen

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun MessageScreen(modifier: Modifier){
    if(true){
        emptyMessageScreen(modifier = modifier)
    }
}

@Composable
fun emptyMessageScreen(modifier:Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ){
            Image(
                painter = painterResource(id = R.drawable.empty_message_icon_by_icons8),
                contentDescription = stringResource(id = R.string.Mail),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .graphicsLayer(alpha = 0.3f)
                    .size(100.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(id = R.string.emptyMessageText),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Gray
            )
        }
    }
}