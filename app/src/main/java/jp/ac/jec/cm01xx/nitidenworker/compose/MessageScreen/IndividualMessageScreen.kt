package jp.ac.jec.cm01xx.nitidenworker.compose.MessageScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.compose.FirebaseViewModel.MessageDetail
import jp.ac.jec.cm01xx.nitidenworker.compose.FirebaseViewModel.Messages

@Composable
fun IndividualMessageScreen(
    sendMessage:(String, Messages) -> Unit
) {

    Scaffold(
        bottomBar = { InputArea() }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn {
                items(count = 2){item ->
                    if(item == 0) MessageItem(bool = true) else MessageItem(bool = false)
                }
            }

            Button(
                onClick = {
//                    sendMessage(
//                        "1234556",
//                        Messages(
//                            documentId = "1234556",
//                            messageContent = listOf(MessageDetail(
//                                senderId = "12344",
//                                messageText = "TextFiled",
//                                timestamp = Timestamp.now()
//                            ))
//                        )
//                    )
                }
            ) {

            }
        }
    }
}

@Composable
fun MessageItem(bool:Boolean) {
    Column(
        horizontalAlignment = if(bool)Alignment.End else Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if(bool) colorResource(id = R.color.nitidenGreen) else Color.LightGray
            ),
            modifier = Modifier
                .padding(vertical = 6.dp, horizontal = 8.dp)
                .widthIn(min = 40.dp, max = 280.dp)
        ) {
            Box{
                Text(
                    text = "dfd",
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun InputArea() {
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val bottomPadding = systemBarsPadding.calculateBottomPadding()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var text by remember { mutableStateOf("") }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let{
                selectedImageUri = it
            }
        }
    )

    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(8.dp)
            .padding(bottom = bottomPadding)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {text = it},
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.LightGray,
                focusedContainerColor = Color.LightGray,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {
                photoPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.image_icon),
                contentDescription = "画像選択",
                tint = Color.Black
            )
        }

        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.Send,
                contentDescription = "送信"
            )
        }
    }
}

//@Preview
//@Composable
//fun TextFiledPreview(){
//    IndividualMessageScreen()
//}