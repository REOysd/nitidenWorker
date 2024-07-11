package jp.ac.jec.cm01xx.nitidenworker.compose.UserScreenFile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun NoUserProfileHeader(
    onClickLoginButton: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(bottom = 200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.unlogin_icon_by_icons8),
                contentDescription = "No Login",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(120.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
            Text(
                text = "ログインされていません",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp),
                color = Color.Gray
            )

            Button(
                onClick = onClickLoginButton,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF00B900))
            ) {
                Text(
                    text = "ログイン",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .align(Alignment.Bottom)
                )
            }
        }
    }
}