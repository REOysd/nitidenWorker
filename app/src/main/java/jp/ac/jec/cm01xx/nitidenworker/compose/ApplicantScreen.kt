package jp.ac.jec.cm01xx.nitidenworker.compose


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import jp.ac.jec.cm01xx.nitidenworker.PublishData
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.UserDocument

@Composable
fun ApplicantScreen(
    publishData: PublishData?,
    applicant: List<UserDocument?>,
    getApplicants:(List<String?>) -> Unit,
    onClickToProfile:() -> Unit,
    modifier: Modifier
){
    LaunchedEffect(publishData) {
        publishData?.let {
            getApplicants(it.applicant)
        }
    }

    publishData?.let{ Data ->
        if (Data.applicant.isEmpty()) {
            EmptyApplicantScreen(modifier = modifier)
        }else{
            ApplicantList(
                applicant = applicant,
                modifier = modifier,
                onClickToProfile = onClickToProfile
            )
        }
    }
}

@Composable
private fun EmptyApplicantScreen(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ){
            Image(
                painter = painterResource(id = R.drawable.unlogin_icon_by_icons8),
                contentDescription = stringResource(id = R.string.Mail),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .graphicsLayer(alpha = 0.3f)
                    .size(120.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.emptyApplicant),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun ApplicantList(
    applicant:List<UserDocument?>,
    onClickToProfile: () -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items = applicant){ item ->
            Row(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .height(70.dp)
                    .drawWithContent {
                        drawContent()
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.5f),
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 0.5.dp.toPx()
                        )
                    }
            ) {
                Spacer(modifier = Modifier.width(4.dp))
                
                item?.let {
                    AsyncImage(
                        model = it.userPhoto,
                        contentDescription = stringResource(id = R.string.UserPhoto_description),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .align(Alignment.CenterVertically)
                            .clickable(
                                onClick = { onClickToProfile() }
                            )
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))

                Column {
                    item?.let {
                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = it.mail,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = it.job,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                FloatingActionButton(
                    containerColor = colorResource(id = R.color.applyButtonColor),
                    shape = FloatingActionButtonDefaults.smallShape,
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .height(40.dp)
                        .width(60.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ){
                        Text(
                            text = stringResource(id = R.string.adoption),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    containerColor = colorResource(id = R.color.nitidenBlue),
                    shape = FloatingActionButtonDefaults.smallShape,
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .height(40.dp)
                        .width(100.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ){
                        Text(
                            text = stringResource(id = R.string.ListenToTheStory),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}