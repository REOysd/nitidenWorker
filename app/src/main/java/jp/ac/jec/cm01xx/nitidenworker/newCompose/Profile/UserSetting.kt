package jp.ac.jec.cm01xx.nitidenworker.newCompose.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun UserSetting(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Setting()
        Spacer(modifier = Modifier.height(20.dp))
        Other()
    }
}

@Composable
private fun Setting() {
    Column {
        Text(
            text = "基本情報",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.hiragino_bold))
            ),
            modifier = Modifier
                .padding(bottom = 6.dp, start = 10.dp)
        )
        Item_Setting(
            imagePainter = painterResource(id = R.drawable.push_notification_icon_by_icons8),
            contentDescription = stringResource(id = R.string.UserProfileScreen_pushNotification_description),
            text = stringResource(id = R.string.UserProfileScreen_pushNotification)
        )
        Item_Setting(
            imagePainter = painterResource(id = R.drawable.baseline_logout_24),
            contentDescription = "logout",
            text = "ログアウト"
        )
    }
}

@Composable
private fun Other() {
    Column {
        Text(
            text = "その他",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.hiragino_bold))
            ),
            modifier = Modifier
                .padding(bottom = 6.dp, start = 10.dp)
        )
        Item_Setting(
            imagePainter = painterResource(id = R.drawable.baseline_contact_support_24),
            contentDescription = "support",
            text = "お問い合わせ"
        )
    }
}

@Composable
private fun Item_Setting(imagePainter: Painter,contentDescription:String,text:String) {
    Card (
        shape = RectangleShape,
        modifier = Modifier
            .background(Color(0xFFEFEDED)),
        onClick = {}
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(48.dp)
                .background(Color(0xFFEFEDED))
        ){
            Spacer(modifier = Modifier.width(18.dp))
            Image(
                painter = imagePainter,
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 11.sp,
                    fontFamily = FontFamily(Font(R.font.hiragino_bold))
                ),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.UserProfileScreen_department_IconButton_description),
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserSettingPreview() {
    UserSetting(modifier = Modifier)
}