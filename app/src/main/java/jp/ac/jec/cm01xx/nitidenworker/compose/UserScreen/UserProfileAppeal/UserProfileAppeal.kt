package jp.ac.jec.cm01xx.nitidenworker.compose.UserScreen.UserProfileAppeal

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import jp.ac.jec.cm01xx.nitidenworker.FirebaseViewModel
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.UserDocument
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun UserProfileAppeal(
    modifier: Modifier,
    userData:UserDocument?,
    firebaseViewModel: FirebaseViewModel
){
    val context = LocalContext.current
    var openBottomSheetOfText by rememberSaveable { mutableStateOf(false) }
    var openBottomSheetOfUrl by rememberSaveable { mutableStateOf(false) }
    val TextsheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val UrlSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val scope = rememberCoroutineScope()
    var text by rememberSaveable { mutableStateOf(userData?.selfPresentation?:"") }


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(bottom = 200.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "自己PR",
                fontSize = 24.sp,
                fontWeight = FontWeight.W900,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.Bottom)
            )
            
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    scope.launch {
                        openBottomSheetOfText = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(end = 20.dp)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "編集",
                    modifier = Modifier

                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .height(0.2.dp)
                .padding(start = 10.dp, end = 10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
                .padding(start = 10.dp, end = 10.dp)
                .clickable(
                    onClick = {
                        scope.launch {
                            openBottomSheetOfText = true
                        }
                    },
                )

        ) {
            Text(
                text = if(userData?.selfPresentation == "") {
                    stringResource(id = R.string.UserProfileAppealExample)
                } else userData?.selfPresentation?:"--",
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 14.sp,
                color = if(userData?.selfPresentation == "")Color.Gray.copy(alpha = 0.5F) else Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .height(0.2.dp)
                .padding(start = 10.dp, end = 10.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        ){
            Text(
                text = "URL",
                fontSize = 18.sp,
                fontWeight = FontWeight.W900,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .align(Alignment.Bottom)
            )
            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = {
                    scope.launch {
                        openBottomSheetOfUrl = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(end = 20.dp)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "編集",
                    modifier = Modifier

                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Divider(
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .height(0.2.dp)
                .padding(start = 10.dp, end = 10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.White)
                .padding(start = 10.dp, end = 10.dp)
                .combinedClickable(
                    onClick = {
                        scope.launch {
                            openBottomSheetOfUrl = true
                        }
                    }
                )
        ) {
            if(userData?.urls?.isEmpty() == true){
                Text(
                    text = stringResource(id = R.string.UserProfileAppealUrlExample),
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 14.sp,
                    color = Color.Gray.copy(alpha = 0.5F),
                )
            }else{
                userData?.urls?.forEach{ url ->
                    if(CheckURL(url)){
                        ClickableText(
                            text = AnnotatedString(
                                text = url,
                                spanStyle = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline,
                                    fontSize = 15.sp
                                )
                            ),
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(context, intent, null)
                            },
                            maxLines = 1,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .fillMaxWidth(),
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    if(openBottomSheetOfText) ModalBottomSheetOnProfileAppealText(
        onDismiss = {openBottomSheetOfText = false},
        sheetState = TextsheetState,
        changeText = {text = it},
        text = text ,
        onClickCheckButton = {
            firebaseViewModel.updateOnMyProfile("selfPresentation", text)
                             },
    )

    if(openBottomSheetOfUrl) ModalBottomSheetOnProfileAppealURL(
        onDismiss = { openBottomSheetOfUrl = false },
        sheetState = UrlSheetState,
        userData = userData,
        updateUrlOnMyProfile = firebaseViewModel::updateUrlOnMyProfile
    )
}

fun CheckURL(url:String):Boolean{
    return Patterns.WEB_URL.matcher(url).matches()
}

