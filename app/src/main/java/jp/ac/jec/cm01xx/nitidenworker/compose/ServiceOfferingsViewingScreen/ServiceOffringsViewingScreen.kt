package jp.ac.jec.cm01xx.nitidenworker.compose.ServiceOfferingsViewingScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.publishData

@Composable
fun ServiceOfferingsViewingScreen(
    serviceOfferings:publishData?,
    isAuthDataVisible:Boolean,
    serviceOfferingsViewingViewModel:ServiceOfferingsViewingViewModel =
        ServiceOfferingsViewingViewModel(serviceOfferings),
    onClickToServiceOfferingsDetailScreen:() -> Unit,
){
    val context = LocalContext.current

    Box{
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp
            ),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            onClick = { onClickToServiceOfferingsDetailScreen() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 120.dp)
                    .padding(start = 10.dp)
            ){
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ){

                    RecruitmentBadge(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ServiceOfferingsViewingImage(
                        selectImages = serviceOfferingsViewingViewModel.selectImages,
                        selectMovies = serviceOfferingsViewingViewModel.selectMovies,
                        selectMovieThumbnail = serviceOfferingsViewingViewModel.selectMovieThumbnail
                    )

                }
                serviceOfferings?.let{
                    ServiceOfferingsViewingItems(
                        timeStamp = serviceOfferingsViewingViewModel.formatTimeStamp(timeStamp = serviceOfferings.timestamp),
                        title = serviceOfferings.title,
                        category = serviceOfferings.category,
                        applyCount = serviceOfferings.applyingCount.toString(),
                        deliveryDays = serviceOfferings.deliveryDays
                    )
                }
            }

            if (isAuthDataVisible){
                AuthData(
                    photoUrl = serviceOfferings?.photoUrl,
                    context = context,
                    name = serviceOfferings?.name,
                    job = serviceOfferings?.job
                )
            }
        }
    }
}

@Composable
fun ServiceOfferingsViewingItems(
    timeStamp:String,
    title:String,
    category: String,
    applyCount:String,
    deliveryDays:String
){
    Column {
        ServiceOfferingsViewingTitle(
            title = title,
            timeStamp = timeStamp,
            modifier = Modifier
                .align(Alignment.End)
        )

        ServiceOfferingsViewingBottomItems(
            category = category,
            applyCount = applyCount,
            deliveryDays = deliveryDays
        )
    }
}

@Composable
fun ServiceOfferingsViewingTitle(
    title: String,
    timeStamp:String,
    modifier: Modifier
){
    Text(
        text = "更新日：${timeStamp}",
        color = Color.Gray,
        fontSize = 8.sp,
        modifier = modifier
            .padding(top = 4.dp, end = 8.dp)
    )

    Spacer(modifier = Modifier.height(3.dp))

    Text(
        text = title,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp,
        modifier = Modifier
            .padding(horizontal = 8.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewServiceOfferingsViewingScreen(){
    ServiceOfferingsViewingScreen(
        serviceOfferings = publishData(
            myUid = "auth.currentUser?.uid.toString()",
            name = "よしだ　れお　さん",
            job = "モバイルアプリケーション開発科",
            photoUrl =
            "https://firebasestorage.googleapis.com/v0/b/nitidenworker.appspot.com/o/UserPhoto%2Fe39fd015-7c5a-4bf8-86db-e65a7e17b609?alt=media&token=a96dabd8-97bb-414a-bb26-459314c76906",
            category = "タイピング",
            title = "これはタイトルです！！よくみていってください",
            subTitle = "a",
            description = "",
            deliveryDays = "12",
            precautions = "",
            selectImages = emptyList(),
            selectMovies = emptyList(),
            checkBoxState = false,
            niceCount = 0,
            favoriteCount = 0,
            applyingCount = 0,
            selectImageThumbnail = null,
        ),
        isAuthDataVisible = true,
        onClickToServiceOfferingsDetailScreen = {}
    )
}



