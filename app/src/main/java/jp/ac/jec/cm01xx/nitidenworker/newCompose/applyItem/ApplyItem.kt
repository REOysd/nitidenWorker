package jp.ac.jec.cm01xx.nitidenworker.newCompose.applyItem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm01xx.nitidenworker.newCompose.applyItem.components.ApplyItemCard
import jp.ac.jec.cm01xx.nitidenworker.newCompose.applyItem.components.Signboard

enum class SignboardType(
    val text: String,
    val backgroundColor: Color
) {
    RECRUITING("現在募集中！", Color(0xFF1FA2E3)),
    TRENDING("急上昇中！", Color(0xFFFF7D19)),
    CLOSING("募集終了", Color(0xFF808080))
}

@Composable
fun ApplyItem(
    signboardType: SignboardType,
    titleText: String,
    categoryText: String,
    expectedCompleteDateText: Int,
    appliedUserCountText: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(horizontal = 16.dp,),
    ) {
        ApplyItemCard(
            titleText = titleText,
            categoryText = categoryText,
            expectedCompleteDateText = expectedCompleteDateText,
            appliedUserCountText = appliedUserCountText,
            modifier = Modifier.align(Alignment.Center)
        )

        Signboard(
            signboardType = signboardType,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = (1).dp, x = (15).dp)
        )
    }
}

@Preview
@Composable
fun ApplyItemPreview() {
    ApplyItem(
        signboardType = SignboardType.TRENDING,
        titleText = "作品のメインタイトル",
        categoryText = "モバイルアプリケーション開発",
        expectedCompleteDateText = 123,
        appliedUserCountText = 124
    )
}