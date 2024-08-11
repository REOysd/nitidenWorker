package jp.ac.jec.cm01xx.nitidenworker.compose.UserScreen.UserProfileAppeal

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.userDocument


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetOnProfileAppealText(
    onDismiss:() -> Unit,
    sheetState: SheetState,
    text:String,
    changeText:(String) -> Unit,
    onClickCheckButton:() -> Unit,
){
    val isExpanded = sheetState.currentValue == SheetValue.Expanded
    val fabOffsetY by animateDpAsState(
        targetValue = if(isExpanded) -30.dp else -390.dp,
    )

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxHeight(),
        containerColor = Color.White,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Text(
                    text = "自己PR",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W900,
                    modifier = Modifier
                        .padding(start = 20.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                        .padding(start = 10.dp, end = 10.dp)
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = changeText,
                    placeholder = {
                        Text(
                            text = "自分のアピールを入力",
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier
                        .padding(20.dp)
                        .padding(bottom = 20.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .defaultMinSize(minHeight = 150.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(y = fabOffsetY, x = -20.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        onDismiss()
                        onClickCheckButton()
                    },
                    modifier = Modifier
                        .padding(bottom = 30.dp, end = 20.dp)
                        .size(60.dp),
                    shape = FloatingActionButtonDefaults.largeShape,
                    containerColor = Color(0xFF00B900),
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetOnProfileAppealURL(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    userData: userDocument?,
    updateUrlOnMyProfile:(List<String>) -> Unit,
){
    val isExpanded = sheetState.currentValue == SheetValue.Expanded
    val fabOffsetY by animateDpAsState(
        targetValue = if(isExpanded) -10.dp else -360.dp,
    )
    var urlInputs by remember { mutableStateOf(userData?.urls?: listOf(""))}
    var urlErrors by remember { mutableStateOf(urlInputs.map { !CheckURL(it) }) }
    var addingButton by remember { mutableStateOf(false) }


    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = Color.White,
        modifier = Modifier
            .fillMaxHeight()
            .defaultMinSize(minHeight = 300.dp)
            .padding(top = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "URL",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W900,
                    modifier = Modifier
                        .padding(start = 20.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                        .padding(start = 10.dp, end = 10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                SmallFloatingActionButton(
                    onClick = {
                        if(!addingButton){
                            if (urlInputs.size < 5) {
                                urlInputs = urlInputs + ""
                                urlErrors = urlErrors + false
                            } else if (urlInputs.size == 5) {
                                addingButton = true
                            }
                        }
                        Log.d("urlInputs",urlInputs.toString())
                        Log.d("urlSize",urlInputs.size.toString())
                    },
                    containerColor = if(addingButton)Color.Gray else Color.White,
                    modifier = Modifier
                        .padding(start = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add URL field",
                        tint = if(addingButton)Color.DarkGray else Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                urlInputs.forEachIndexed{ index, url ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color.White)
                    ){
                        OutlinedTextField(
                            value = url,
                            onValueChange = { newText ->
                                urlInputs = urlInputs.toMutableList().also { it[index] = newText }
                                urlErrors = urlErrors.toMutableList().also { it[index] = !CheckURL(newText) }
                            },
                            maxLines = 1,
                            placeholder = {
                                Text(
                                    text = "URLを記入",
                                    color = Color.Gray.copy(alpha = 0.5f),
                                    fontSize = 14.sp
                                )
                            },
                            isError = urlErrors[index],
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 20.dp, end = 10.dp)
                        )

                        IconButton(
                            onClick = {
                                if(urlInputs.size > 1){
                                    urlInputs = urlInputs.filterIndexed { i, _ -> i != index }
                                    urlErrors = urlErrors.filterIndexed { i, _ -> i != index }
                                    addingButton = false
                                }else{
                                    urlInputs = listOf("")
                                    urlErrors = listOf(false)
                                }
                            },
                            modifier = Modifier
                                .padding(end = 14.dp)
                                .align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "delete"
                            )
                        }
                    }
                    if (urlErrors[index]) {
                        Text(
                            text = "入力されたURLは正しくありません",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                }

            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(y = fabOffsetY, x = -20.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        if(!urlErrors.contains(true)){
                            updateUrlOnMyProfile(urlInputs.filter { it.isNotBlank() })
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .padding(bottom = 30.dp, end = 20.dp)
                        .size(60.dp),
                    shape = FloatingActionButtonDefaults.largeShape,
                    containerColor = if(!urlErrors.contains(true))Color(0xFF00B900)else Color.Gray,

                    ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = if(!urlErrors.contains(true))Color.White else Color.DarkGray
                    )
                }
            }
        }
    }
}