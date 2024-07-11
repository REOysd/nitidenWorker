package jp.ac.jec.cm01xx.nitidenworker.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseUser
import jp.ac.jec.cm01xx.nitidenworker.FirebaseViewModel
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun UserScreen(
    modifier: Modifier,
    currentUser:FirebaseUser?,
    onClickLoginButton:() -> Unit,
    onClickLogoutButton:() -> Unit,
){
    var ProfileCurrentUser by rememberSaveable { mutableStateOf(currentUser) }
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
    ){
        if(ProfileCurrentUser == null){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(bottom = 200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.unlogin_icon_by_icons8),
                    contentDescription = "No Login",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(120.dp),
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
                Text(
                    text = "ログインされていません",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp),
                    color = Color.Gray
                )

                Button(
                    onClick = onClickLoginButton,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 50.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF00B900))
                ) {
                    Text(
                        text = "ログイン",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                    )
                }
            }
        }else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(200.dp)
                        .drawWithContent {
                            drawContent()
                            // 下部にのみボーダーを描画
                            drawLine(
                                color = Color.Gray,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 0.3.dp.toPx()
                            )
                        }
                ) {
                    ProfileCurrentUser?.photoUrl?.let{
                        Spacer(modifier = Modifier.height(12.dp))
                        AsyncImage(
                            model = ImageRequest.Builder(context = context)
                                .data(it)
                                .crossfade(true)
                                .build(),
                            contentDescription = "ProfileImage",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(100.dp)
                                .clip(RoundedCornerShape(80.dp))
                        )
                    }

                    ProfileCurrentUser?.displayName?.let{
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = it,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            fontWeight = FontWeight(1000),
                            fontSize = 20.sp
                        )
                    }

                    ProfileCurrentUser?.uid?.let{
                        Spacer(modifier = Modifier.height((8.dp)))
                        Text(
                            text = "ID:${it}",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                        )
                    }
                }
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
                            text = "--件",
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }

                    Divider(
                        color = Color.Gray,
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
                            text = "--％",
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

                    Divider(
                        color = Color.Gray,
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
                               .size(40.dp)
                               .align(Alignment.CenterVertically),
                           colorFilter = ColorFilter.tint(Color.Gray)
                       )
                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = ) {

                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserScreenPreview(){
    val firebaseViewModel = FirebaseViewModel()

    UserScreen(
        modifier = Modifier,
        currentUser = null,
        onClickLoginButton = {},
        onClickLogoutButton = {}
    )
}