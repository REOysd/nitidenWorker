package jp.ac.jec.cm01xx.nitidenworker.newCompose.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun UserInformationButton(
    height: Dp = 80.dp,
    userName:String,
    studentID:String,
    userIconSize: Dp = 60.dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(Color.White)
            .clickable(
                indication = rememberRipple(bounded = true),
                interactionSource = remember { MutableInteractionSource() }
            ) {
            }
    ){
        Spacer(modifier = Modifier.width(15.dp))
        Surface(
            modifier = Modifier
                .size(userIconSize)
                .align(Alignment.CenterVertically),
            shape = CircleShape,
            color = Color.LightGray,
        ) {}
        Spacer(modifier = Modifier.width(15.dp))
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .background(Color.White)
                .weight(1f)
        ) {
            Text(
                text = userName,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.hiragino_black)),
                    fontSize = 16.sp
                ),
            )
            Text(
                text = studentID,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.hiragino_black)),
                    fontSize = 12.sp,
                    color = Color(0xFF909090)
                ),
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = "backArrowIcon",
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically),
        )
        Spacer(modifier = Modifier.width(15.dp))
    }
}