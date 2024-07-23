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
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.FirebaseUser
import jp.ac.jec.cm01xx.nitidenworker.FirebaseViewModel
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.UserDocument
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen.ServiceOfferingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServiceOfferingsDetailScreen(
    firebaseViewModel: FirebaseViewModel,
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
    var niceCount by rememberSaveable { mutableStateOf(0) }
    var favoriteCount by rememberSaveable { mutableStateOf(0) }
    val currentUser = firebaseViewModel.auth.currentUser
    val userData by firebaseViewModel.userData.collectAsState()

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
                    fontSize = 20.sp,
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
                        .padding(bottom = 8.dp)
                ) {

                    Column (
                        modifier = Modifier
                            .padding(start = 12.dp, end = 16.dp)
                    ){
                        Text(
                            text = "イイね数 : ${niceCount}",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            modifier = Modifier
                        )

                        Text(
                            text = "お気に入り登録数 : ${favoriteCount}",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            modifier = Modifier
                        )

                        Text(
                            text = "応募した人数 : 0",
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            modifier = Modifier
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ){
                        HeartIcon(
                            onChangeNiceCount = {
                                niceCount += it
                            }
                        )

                        FavoriteIcon(
                            onChangeFavoriteCount = {
                                favoriteCount += it
                            }
                        )
                    }
                }
            }

            MyProfileItems(
                currentUser = currentUser,
                context = context,
                userData = userData
            )
        }
    }
}


@Composable
fun RatingStar(
    completionRate:Float,
    maxRating:Int = 5
    ){
    Row{
        for(i in 1..maxRating){
            when{
                i <= completionRate.toInt() -> {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                }
                else -> {
                    Icon(
                        imageVector = Icons.Outlined.Star,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Text(
            text = "（完了率:${completionRate}）",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(start = 4.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun HeartIcon(
    onChangeNiceCount:(Int) -> Unit
){
    val heart by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.heart_lottie))
    var isLiked by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (isLiked) 0.6f else 0f,
        animationSpec = tween(durationMillis = 900)
    )

    IconButton(
        onClick = {
            isLiked = !isLiked
            if(isLiked){
                onChangeNiceCount(1)
            }else{
                onChangeNiceCount(-1)
            }
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
fun FavoriteIcon(
    onChangeFavoriteCount:(Int) -> Unit,
) {
    val heart by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.favorite_lottie))
    var isLiked by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (isLiked) 1f else 0f,
        animationSpec = tween(durationMillis = 900)
    )

    IconButton(
        onClick = {
            isLiked = !isLiked
            if(isLiked){
                onChangeFavoriteCount(1)
            }else{
                onChangeFavoriteCount(-1)
            }
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
            .height(170.dp)
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
                text = "※画像、動画を選択しなかった場合,こちらの画像がデフォルトで表示されます。",
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyProfileItems(
    currentUser:FirebaseUser?,
    context:Context,
    userData:UserDocument?,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .combinedClickable(
                onClick = {

                }
            )
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

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .background(Color.White)
                .padding(top = 8.dp, bottom = 8.dp, start = 12.dp)
        ) {
            currentUser?.email?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }

            Spacer(
                modifier = Modifier
                    .width(12.dp)
                    .height(8.dp)
            )

            userData?.job?.let {
                Text(
                    text = "学科 : ${it}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            userData?.numberOfAchievement?.let {
                Text(
                    text = "実績数 : ${it}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))


            userData?.completionRate?.let {
                Text(
                    text = "総イイね数 : ${it}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }

            RatingStar(completionRate = 4.2f)
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .background(Color.White)
                .align(Alignment.CenterVertically)
                .padding(end = 20.dp)
        ) {
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

@Preview(showBackground = true)
@Composable
fun ServiceOfferingsDetailScreenPreview(){
    ServiceOfferingsDetailScreen(
        data = ServiceOfferingData(
            category = "category",
            title = "タイトルはこれです。以上です",
            subTitle = "サブタイトルはこちらです。以上ですfgsdfgsdfgsdgsdfgsdfgsdfgsdf\n" +
                    "dasdfadfasdfafdasdfsdasdfadsfadsfsdfasdfasdfasdfasfadfds\n",
            description = "詳細な説明はこちらです。以上です",
            deliveryDays = "12",
            precautions = "precautionsText",
            selectImages = listOf(null),
            selectMovies = emptyList(),
            checkBoxState = false
        ),
        onClickToPopBackStack = { /*TODO*/ },
        modifier = Modifier,
        firebaseViewModel = FirebaseViewModel()
    )
}
