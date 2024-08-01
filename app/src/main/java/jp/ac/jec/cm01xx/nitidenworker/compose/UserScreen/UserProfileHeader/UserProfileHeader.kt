package jp.ac.jec.cm01xx.nitidenworker.compose.UserScreen.UserProfileHeader

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.UserDocument
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    modifier: Modifier,
    onClickLogoutButton: () -> Unit,
    onClickCheckButton:(String,String) -> Unit,
    SwitchProfileCurrentUser:() -> Unit,
    userData:UserDocument?,
    uid:String?
){
    var openBottomSheetOfDepartment by rememberSaveable { mutableStateOf(false) }
    val DepartmentSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(bottom = 60.dp)
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .background(Color.Gray)
                .height(60.dp)
                .fillMaxWidth(),
        ) {

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "実績数",
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${userData?.numberOfAchievement}件",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            HorizontalDivider(
                color = Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier
                    .width(0.2.dp)
            )
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = "完了率",
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${userData?.completionRate}％",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(90.dp)
        ) {
            Text(
                text = "基本情報",
                fontSize = 16.sp,
                fontWeight = FontWeight.W900,
                modifier = Modifier
                    .padding(start = 10.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = Color.Gray.copy(alpha = 0.3f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.2.dp)
                    .padding(start = 10.dp, end = 10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icons8__60___),
                    contentDescription = "department",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier

                        .height(80.dp)
                ) {
                    Text(
                        text = "学科",
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        fontSize = 10.sp
                    )
                    if(userData?.uid == uid){
                        Text(
                            text = if (userData?.job == "--") "＜例＞モバイルアプリケーション開発科" else "${userData?.job}",
                            color = if (userData?.job == "--") Color.Gray.copy(alpha = 0.5f) else Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600
                        )
                    }else{
                        Text(
                            text = "${userData?.job}",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W600
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if(uid == userData?.uid){
                    IconButton(
                        onClick = { scope.launch { openBottomSheetOfDepartment = true } },
                        modifier = Modifier
                            .size(70.dp)
                            .align(Alignment.Top)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "編集"
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        if(uid == userData?.uid){
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .height(90.dp)
            ) {
                Text(
                    text = "設定",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W900,
                    modifier = Modifier
                        .padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    color = Color.Gray.copy(alpha = 0.3f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                        .padding(start = 10.dp, end = 10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.push_notification_icon_by_icons8),
                        contentDescription = "department",
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .size(24.dp)
                            .align(Alignment.CenterVertically),
                        colorFilter = ColorFilter.tint(Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "プッシュ通知",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W600,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    )
                }
            }
            Spacer(modifier = Modifier.height(150.dp))

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .height(90.dp)
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = Color.Gray,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 0.3.dp.toPx()
                        )
                    }
            ) {
                TextButton(
                    onClick = {
                        onClickLogoutButton()
                        SwitchProfileCurrentUser()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "ログアウト",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600,
                    )
                }
            }
        }
    }

    if(openBottomSheetOfDepartment){
        ModalBottomSheetOnProfileHeader(
            bottomSheetTitle = "学科",
            placeholderOnTextField = "自分の所属する学科を記入してください",
            onDismiss = { openBottomSheetOfDepartment = false },
            sheetState = DepartmentSheetState,
            onClickCheckButton = onClickCheckButton
        )
    }
}