package jp.ac.jec.cm01xx.nitidenworker.newCompose.Message

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.newCompose.common.HomeTopBar

@Composable
fun MessageScreen() {
    Scaffold(
        topBar = {
            HomeTopBar(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
        )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            items(messageList) { message ->
                MessageItem(message)
            }
        }
    }
}


@Composable
private fun MessageItem(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            ) {
                //TODO
            }
            .padding(16.dp)
        ,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = Color.LightGray
            ) {
                // Avatar placeholder
            }
            if (message.isOnline) {
                Surface(
                    modifier = Modifier
                        .size(15.dp)
                        .border(
                            width = 1.6.dp,
                            color = Color.White,
                            shape = CircleShape
                        ),
                    shape = CircleShape,
                    color = Color(0xFF22C55E)
                ) {}
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.username,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.hiragino_black))
                    )
                )
                Text(
                    text = message.date,
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (message.message.length > 20) {
                    message.message.substring(0, 20) + "..."
                } else {
                    message.message
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily(Font(R.font.hiragino_bold)),
                    color = if (message.isOnline) Color.Black else Color.Gray,
                    )
            )
        }
    }
}

data class Message(
    val id: Int,
    val username: String,
    val message: String,
    val date: String,
    val isOnline: Boolean = false
)

private val messageList = listOf(
    Message(
        id = 1,
        username = "ユーザーネーム",
        message = "こんにちは！あなたの作品とても興味を持ちました！",
        date = "昨日",
        isOnline = true
    ),
    Message(
        id = 2,
        username = "ユーザーネーム",
        message = "近々、お話できる時間あるのでもし良ければお話ししませんか？",
        date = "2024/11/3"
    ),
    Message(
        id = 3,
        username = "ユーザーネーム",
        message = "おもしろいところ発見したので、よろしければ授業にお誘いしてもよろしいですか？",
        date = "2024/10/29"
    ),
    Message(
        id = 4,
        username = "ユーザーネーム",
        message = "報告の良い形にお時間いただきまして日程調整をしていただければなと思います。",
        date = "2024/10/19"
    )
)

@Preview(showBackground = true)
@Composable
fun MessageScreenPreview() {
    MessageScreen()
}