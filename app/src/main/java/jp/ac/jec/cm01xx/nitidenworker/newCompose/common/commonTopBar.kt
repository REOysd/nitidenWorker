package jp.ac.jec.cm01xx.nitidenworker.newCompose.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun CommonTopBar(
    modifier: Modifier = Modifier,
    showBackButton: Boolean = true,
    showUserIcon: Boolean = false,
    onClickToBack: (() -> Unit)? = null,
    topAppBarTitle: String,
    isCenterAlignment: Boolean = true,
) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color.White)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .height(60.dp)
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color(0xFFB5B1B1),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2.dp.toPx()
                )
            }
    ) {
        if (showBackButton) {
            IconButton(
                onClick = { onClickToBack?.let { it() } },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = "backIcon",
                    modifier = Modifier.size(35.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .then(
                    if (isCenterAlignment) Modifier.align(Alignment.Center)
                    else Modifier.align(Alignment.CenterStart).padding(start = 52.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showUserIcon) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color.LightGray,
                    content = {}
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Text(
                text = topAppBarTitle,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.hiragino_black))
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonTopBarPreview() {
    CommonTopBar(
        modifier = Modifier,
        showBackButton = true,
        showUserIcon = false,
        topAppBarTitle = "ユーザーネーム",
        isCenterAlignment = true
    )
}