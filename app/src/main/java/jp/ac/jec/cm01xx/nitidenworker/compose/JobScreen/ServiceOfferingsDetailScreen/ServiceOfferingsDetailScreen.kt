package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen.ServiceOfferingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServiceOfferingsDetailScreen(
    data:ServiceOfferingData,
    onClickToPopBackStack:() -> Unit,
    modifier: Modifier
){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val images = data.selectImages.filterNotNull()
    val movies = data.selectMovies.filterNotNull()
    val selectImageAndMovie = images + movies
    val selectImageAndMoviePageCount = images.size + movies.size
    val selectImageAndMoviePagerState = rememberPagerState(
        pageCount = {selectImageAndMoviePageCount},
        initialPage = 0
    )


    Scaffold(
        topBar = {
            ServiceOfferingsDetailTopBar(
                onClickToPopBackStack = onClickToPopBackStack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(innerPadding)
        ) {
            ImageAndVideoThumbnail(
                scope = scope,
                selectImageAndMoviePagerState = selectImageAndMoviePagerState,
                images = images,
                selectImageAndMovie = selectImageAndMovie,
                context = context,
                selectImageAndMoviePageCount = selectImageAndMoviePageCount
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp)
                    .background(Color.White)
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
                Text(
                    text = data.title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(10.dp)
                )


                Text(
                    text = data.subTitle,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 6.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    HeartIcon()
                    FavoriteIcon()
                }
            }
        }
    }
}

@Composable
fun HeartIcon(){
    val heart by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.heart_lottie))
    var isLiked by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (isLiked) 0.6f else 0f,
        animationSpec = tween(durationMillis = 900) // アニメーション速度を調整
    )

    IconButton(
        onClick = {
            isLiked = !isLiked
        },
        modifier = Modifier.size(50.dp)
    ) {
        LottieAnimation(
            composition = heart,
            progress = { animatedProgress }
        )
    }
}


@Composable
fun FavoriteIcon() {
    val heart by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.favorite_lottie))
    var isLiked by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (isLiked) 1f else 0f,
        animationSpec = tween(durationMillis = 900) // アニメーション速度を調整
    )

    IconButton(
        onClick = {
            isLiked = !isLiked
        },
        modifier = Modifier.size(50.dp)
    ) {
        LottieAnimation(
            composition = heart,
            progress = { animatedProgress }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageAndVideoThumbnail(
    scope:CoroutineScope,
    selectImageAndMoviePagerState:PagerState,
    images:List<Uri?>,
    selectImageAndMovie:List<Uri?>,
    context: Context,
    selectImageAndMoviePageCount:Int
    ) {
    Box(
        modifier = Modifier
            .height(200.dp)
            .background(Color.Black)
            .clickable(
                onClick = {}
            )
    ){

        if(selectImageAndMovie.isEmpty()== true){
            Image(
                painter = painterResource(id = R.drawable.nitiiden_icon),
                contentDescription = "normalImage",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp)
            )

            Text(
                text = "※画像、動画を選択しなかった場合,\nこちらの画像がデフォルトで表示されます。",
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            )
        }else{
            HorizontalPager(
                state = selectImageAndMoviePagerState,
                modifier = Modifier
                    .background(Color.Black)
                    .height(200.dp)
            ) { page ->

                if(images.size <= page){
                    var thumbnail by rememberSaveable { mutableStateOf<Bitmap?>(null) }

                    LaunchedEffect(selectImageAndMovie[page]) {
                        withContext(Dispatchers.IO) {
                            try {
                                val uri = Uri.parse(selectImageAndMovie[page].toString())
                                context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
                                    val retriever = MediaMetadataRetriever()
                                    retriever.setDataSource(pfd.fileDescriptor)
                                    thumbnail = retriever.frameAtTime
                                    retriever.release()
                                }
                            } catch (e: Exception) {
                                Log.e("サムネイル", "Error: ${e.message}", e)
                            }
                        }
                    }

                    thumbnail?.let {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ){
                            Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                            )

                            IconButton(
                                onClick = { },
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(50.dp),
                                colors = IconButtonDefaults.iconButtonColors(Color.White)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "play video",
                                    modifier = Modifier
                                        .size(40.dp),
                                    tint = Color.Black
                                )
                            }
                        }
                    }

                }else{
                    AsyncImage(
                        model =  selectImageAndMovie[page],
                        contentDescription = "selectImageAndMovie",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(selectImageAndMoviePageCount) { iteration ->
                    val color =
                        if (selectImageAndMoviePagerState.currentPage == iteration) Color.DarkGray
                        else Color.LightGray

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(6.dp)
                            .clip(CircleShape)
                            .background(color)
                            .align(Alignment.Top)
                            .clickable(
                                onClick = {
                                    scope.launch {
                                        selectImageAndMoviePagerState
                                            .animateScrollToPage(iteration)
                                    }
                                }
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServiceOfferingsDetailScreenPreview(){
    ServiceOfferingsDetailScreen(
        data = ServiceOfferingData(
            category = "category",
            title = "タイトルはこれです。以上です",
            subTitle = "サブタイトルはこちらです。以上です",
            description = "詳細な説明はこちらです。以上です",
            deliveryDays = "12",
            precautions = "precautionsText",
            selectImages = listOf(null),
            selectMovies = emptyList(),
            checkBoxState = false
        ),
        onClickToPopBackStack = { /*TODO*/ },
        modifier = Modifier
    )
}