package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun ServiceOfferingCreationTopBar(
    onClickToPopBackStack:() -> Unit
){
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val height = systemBarsPadding.calculateTopPadding()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = height)
            .height(60.dp)
            .background(Color.White)
            .drawWithContent {
                drawContent()
                // 下部にのみボーダーを描画
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 0.5.dp.toPx()
                )
            }
    ){
        IconButton(
            onClick = {
                onClickToPopBackStack()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 18.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.ArrowBack_Icon_description)
            )
        }
        Text(
            text = stringResource(id = R.string.ServiceOfferingCreationTopBar_title),
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.Center),
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.W500
        )
    }
}