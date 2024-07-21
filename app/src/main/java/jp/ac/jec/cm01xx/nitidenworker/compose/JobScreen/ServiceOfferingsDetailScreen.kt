package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen.ServiceOfferingData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServiceOfferingsDetailScreen(
    data:ServiceOfferingData,
    modifier: Modifier
){
    val images = data.selectImages.filterNotNull()
    val imageSelectPageCount = images.size
    val imageSelectPagerState = rememberPagerState(
        pageCount = {imageSelectPageCount},
        initialPage = 0
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        HorizontalPager(
            state = imageSelectPagerState,
            modifier = Modifier
                .background(Color.White)
                .height(200.dp)
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
            ){
                Text(text = "dfafadss")
                Image(
                    painter = rememberAsyncImagePainter(
                        model = images[page],
                        contentScale = ContentScale.Crop
                    ),
                    contentDescription = "selectImage",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                            .fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServiceOfferingsPreview(){
    ServiceOfferingsDetailScreen(
        data = ServiceOfferingData(
            category = "dfdfsadfd",
            title = "dfaadsfasd",
            subTitle = "dfadfasdfadfasf",
            description = "fadafadfaf",
            deliveryDays = "12",
            precautions = "dsfadfdsfd",
            selectImages = listOf(null),
            selectMovies = listOf(null),
            checkBoxState = false
        ),
        modifier = Modifier
    )
}