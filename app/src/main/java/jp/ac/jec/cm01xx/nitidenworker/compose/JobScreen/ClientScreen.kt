package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.ac.jec.cm01xx.nitidenworker.FirebaseViewModel
import jp.ac.jec.cm01xx.nitidenworker.UserDocument

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ClientScreen(
    modifier: Modifier,
    onClickToProfile:() -> Unit,
    userData:UserDocument?,
    firebaseViewModel: FirebaseViewModel
){
    val currentUser = firebaseViewModel.auth.currentUser
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .height(90.dp)
                .combinedClickable(
                    onClick = onClickToProfile
                )
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .height(90.dp)
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.5f),
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 0.5.dp.toPx()
                        )
                    }
            ){
                Spacer(modifier = Modifier.width(12.dp))

                currentUser?.photoUrl?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(context = context)
                            .data(it)
                            .crossfade(true)
                            .build(),
                        contentDescription = "ProfileImage",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(40.dp))
                            .align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .background(Color.White)
                ){
                    currentUser?.email?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    userData?.job?.let {
                        Text(
                            text = userData.job,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .align(Alignment.CenterVertically)
                        .padding(end = 20.dp)
                ){
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "ToProfile",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(24.dp)
                    )
                }
            }
        }
    }
}