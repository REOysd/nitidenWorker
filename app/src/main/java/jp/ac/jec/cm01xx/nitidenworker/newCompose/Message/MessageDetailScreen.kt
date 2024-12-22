package jp.ac.jec.cm01xx.nitidenworker.newCompose.Message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.newCompose.common.CommonTopBar

data class ChatMessage(
    val id: Int,
    val content: String,
    val isOwnMessage: Boolean,
    val isImage: Boolean = false,
    val timestamp: String = ""
)

@Composable
fun MessageDetailScreen() { 
    var messageText by rememberSaveable { mutableStateOf("") }
    val messages = listOf(
        ChatMessage(1, "こんにちは", false),
        ChatMessage(2, "はじめまして！よろしくお願いします。", true),
        ChatMessage(3, "今日はいい天気ですね", false),
        ChatMessage(4, "そうですね！散歩日和です。", true),
        ChatMessage(5, "長めのメッセージテストです。これは複数行に渡るメッセージの例として表示されます。メッセージの長さを調整して、UIがどのように表示されるかを確認しています。", true),
        ChatMessage(6, "", true, isImage = true),
        ChatMessage(7, "先日の会議の件について相談させていただきたいのですが、お時間はありますでしょうか？", false),
        ChatMessage(8, "もちろんです！具体的にどのような内容でしょうか？", true),
        ChatMessage(9, "プロジェクトの進行状況について、いくつか気になる点がありまして...", false),
        ChatMessage(10, "承知しました。それについては、以下のような対応を考えています。", true),
        ChatMessage(11, "", false, isImage = true),
        ChatMessage(12, "先ほどの資料を確認させていただきました。", false,isImage = false),
        ChatMessage(13, "ご確認ありがとうございます。修正点などございましたらお知らせください。", true),
        ChatMessage(14, "はい、特に問題ありません。このまま進めていきましょう。", false),
        ChatMessage(15, "長めのメッセージの例です。これは複数行にわたるテキストで、メッセージバブルのレイアウトとスクロールの動作を確認するためのものです。実際のチャットでもこのように長いメッセージが送信されることがあります。", true),
        ChatMessage(16, "では、次回の打ち合わせまでに準備を進めておきます。", true),
        ChatMessage(17, "お願いします。何か不明点があればご連絡ください。", false),
        ChatMessage(18, "来週の予定について確認したいのですが", false),
        ChatMessage(19, "はい、来週の予定ですが、午後であれば比較的時間が取れそうです。", true),
        ChatMessage(20, "プロジェクトの進捗状況をまとめた資料です。", false, isImage = true)
    )
    

    Scaffold(
        topBar = {
            CommonTopBar(
                modifier = Modifier,
                showBackButton = true,
                onClickToBack = null,
                topAppBarTitle = "ユーザーネーム",
                showUserIcon = true,
                isCenterAlignment = false
            )
        },
        bottomBar = {
            MessageInputField(
                value = messageText,
                onValueChange = {messageText = it},
                onSendClick = { /*TODO*/ }) {

            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(messages) { message ->
                MessageItem(message = message)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MessageItem(message: ChatMessage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = if (!message.isOwnMessage) 8.dp else 0.dp,
                end = if (message.isOwnMessage) 8.dp else 0.dp
            ),
        horizontalArrangement = if (message.isOwnMessage) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isOwnMessage) {
            Surface(
                modifier = Modifier.size(34.dp),
                shape = CircleShape,
                color = Color.LightGray
            ) { }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column {
            if (!message.isOwnMessage) {
                Spacer(modifier = Modifier.height(20.dp))
            }

            if (message.isImage) {
                Surface(
                    modifier = Modifier
                        .width(200.dp)
                        .height(250.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Gray
                ) { }
                if (message.content.isNotEmpty()) Spacer(modifier = Modifier.height(8.dp))
            }
            if (message.content.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(
                        topStart = if (message.isOwnMessage) 12.dp else 0.dp,
                        topEnd = if (message.isOwnMessage) 0.dp else 12.dp,
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    ),
                    color = if (message.isOwnMessage) Color(0xFF4CAF50) else Color.LightGray,
                    modifier = Modifier.widthIn(max = 300.dp)
                ) {
                    Text(
                        text = message.content,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        color = if (message.isOwnMessage) Color.White else Color.Black,
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
            }
        }
    }
}


@Composable
fun MessageInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onAttachmentClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFD1CECE))
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 45.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.hiragino_regular))
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    decorationBox = { innerTextField ->
                        Box {
                            if (value.isEmpty()) {
                                Text(
                                    "メッセージを入力してください",
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily(Font(R.font.hiragino_regular))
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                Box(
                    modifier = Modifier.padding(end = 12.dp, top = 6.dp)
                ) {
                    IconButton(
                        onClick = onAttachmentClick,
                        modifier = Modifier
                            .size(32.dp)
                    ) {
                        Icon(painter = painterResource(id = R.drawable.baseline_image_24),
                            contentDescription = "Add attachment",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier.padding(top = 2.dp)
        ) {
            FloatingActionButton(
                onClick = onSendClick,
                containerColor = Color(0xFF45C152),
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Send,
                    contentDescription = "Send message",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageDetailScreenPreview() {
    MessageDetailScreen()
}