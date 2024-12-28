package jp.ac.jec.cm01xx.nitidenworker.newCompose.applyItem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun ApplyItemContent(
    titleText: String,
    categoryText: String,
    expectedCompleteDateText: Int,
    appliedUserCountText: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top
    ) {
        ApplyItemImage()
        ApplyItemDetails(
            titleText = titleText,
            categoryText = categoryText,
            expectedCompleteDateText = expectedCompleteDateText,
            appliedUserCountText = appliedUserCountText
        )
    }
}

@Composable
private fun ApplyItemDetails(
    titleText: String,
    categoryText: String,
    expectedCompleteDateText: Int,
    appliedUserCountText: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = titleText,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.hiragino_black))
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        ApplyItemInformation(
            categoryText = categoryText,
            expectedCompleteDateText = expectedCompleteDateText,
            appliedUserCountText = appliedUserCountText
        )
    }
}

@Composable
private fun ApplyItemImage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(start = 20.dp, top = 25.dp)
            .size(70.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFD9D9D9),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
fun ApplyItemInformation(
    categoryText: String,
    expectedCompleteDateText: Int,
    appliedUserCountText: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "カテゴリー",
                style = TextStyle(
                    fontSize = 9.sp,
                    fontFamily = FontFamily(Font(R.font.hiragino_medium))
                )
            )
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFF45C152),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .height(18.dp)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = categoryText,
                    color = Color(0xFF45C152),
                    style = TextStyle(
                        fontSize = 7.sp,
                        fontFamily = FontFamily(Font(R.font.hiragino_black))
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "想定完成期間",
                style = TextStyle(
                    fontSize = 9.sp,
                    fontFamily = FontFamily(Font(R.font.hiragino_medium))
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = expectedCompleteDateText.toString(),
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "日",
                    style = TextStyle(
                        fontSize = 9.sp,
                        fontFamily = FontFamily(Font(R.font.hiragino_regular))
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "応募している人数",
                style = TextStyle(
                    fontSize = 9.sp,
                    fontFamily = FontFamily(Font(R.font.hiragino_medium))
                )
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = appliedUserCountText.toString(),
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "人",
                    style = TextStyle(
                        fontSize = 9.sp,
                        fontFamily = FontFamily(Font(R.font.hiragino_regular))
                    )
                )
            }
        }
    }
}