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
import androidx.compose.foundation.border
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.lifecycle.viewmodel.compose.viewModel
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
    viewModel:ServiceOfferingsDetailViewModel = viewModel(),
    firebaseViewModel: FirebaseViewModel,
    data:ServiceOfferingData,
    onClickToPopBackStack:() -> Unit,
    modifier: Modifier
){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val selectImageAndMovie by viewModel.selectImageAndMovie.collectAsState()
    val selectImageAndMoviePagerState = rememberPagerState(
        pageCount = {uiState.selectImageAndMoviePageCount},
        initialPage = 0
    )
    val currentUser = firebaseViewModel.auth.currentUser
    val userData by firebaseViewModel.userData.collectAsState()
    
    LaunchedEffect(data) {
        viewModel.initializeData(data)
    }

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
                images = uiState.images,
                selectImageAndMovie = selectImageAndMovie,
                context = context,
                selectImageAndMoviePageCount = uiState.selectImageAndMoviePageCount
            )
            
            TitleAndSubTitleBar(
                title = uiState.title,
                subTitle = uiState.subTitle,
                niceCount = uiState.niceCount,
                favoriteCount = uiState.favoriteCount,
                onChangeNiceCount = { viewModel.onChangedNiceCount(it) },
                onChangeFavoriteCount = { viewModel.onChangedFavoriteCount(it) }
            )

            MyProfileItems(
                currentUser = currentUser,
                context = context,
                userData = userData
            )


            BottomItemBar(
                category = data.category,
                deliveryDays = data.deliveryDays,
                checkBoxState = data.checkBoxState,
                description = data.description,
                precautions = data.precautions?:"",
            )
        }
    }
}


@Composable
fun TitleAndSubTitleBar(
    title:String,
    subTitle:String,
    niceCount:Int,
    favoriteCount:Int,
    onChangeNiceCount:(Boolean) -> Unit,
    onChangeFavoriteCount: (Boolean) -> Unit
){
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
            text = title,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(10.dp)
        )

        if(subTitle.isNotEmpty()){
            Text(
                text = subTitle,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ){
                HeartIcon(
                    onChangeNiceCount = {
                        onChangeNiceCount(it)
                    }
                )

                Text(
                    text = niceCount.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )

                FavoriteIcon(
                    onChangeFavoriteCount = {
                        onChangeFavoriteCount(it)
                    }
                )

                Text(
                    text = favoriteCount.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

@Composable
fun RatingStar(
    completionRate:Float?,
    maxRating:Int = 5
    ){
    Row{
        completionRate?.let{
            for (i in 1..maxRating) {
                when {
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
    onChangeNiceCount:(Boolean) -> Unit
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
                onChangeNiceCount(true)
            }else{
                onChangeNiceCount(false)
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
    onChangeFavoriteCount:(Boolean) -> Unit,
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
                onChangeFavoriteCount(true)
            }else{
                onChangeFavoriteCount(false)
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
                .padding(top = 4.dp, bottom = 4.dp, start = 12.dp)
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
                    .height(6.dp)
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

            Spacer(modifier = Modifier.height(4.dp))

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

            Spacer(modifier = Modifier.height(4.dp))


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

            Spacer(modifier = Modifier.height(4.dp))

            RatingStar(
                completionRate = if(userData?.completionRate == "--"){
                    0.0f
                }else{
                    userData?.completionRate?.toFloat()
                }
            )
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

@Composable
fun BottomItemBar(
    category:String,
    deliveryDays:String,
    checkBoxState:Boolean,
    description:String,
    precautions:String
){
    Column {
        Spacer(modifier = Modifier.height(20.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 20.dp)
        ){
            Text(
                text = "カテゴリー",
                fontWeight = FontWeight.W500,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = category,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF45c152),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .border(
                        color = Color(0xFF45c152),
                        width = 1.dp,
                        shape = RoundedCornerShape(5.dp),
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 20.dp)
        ){
            Text(
                text = "応募してる人数",
                fontWeight = FontWeight.W500,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "0",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )

            Text(
                text = "　人",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )


        }

        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 20.dp)
        ){
            Text(
                text = "予想お届け日数",
                fontWeight = FontWeight.W500,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = deliveryDays,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )

            Text(
                text = "　日",
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 22.dp)
        ){
            Text(
                text = "外部サイトでのビデオチャット",
                fontWeight = FontWeight.W500,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if(checkBoxState) "あり" else "なし",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )
        }

        Spacer(modifier = Modifier.height(46.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
        ) {
            Text(
                text = "サービス内容の説明",
                fontWeight = FontWeight.W900,
                fontSize = 16.sp,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                color = Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.2.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )

        }

        if(precautions.isNotEmpty()){
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = "注意事項の説明",
                    fontWeight = FontWeight.W900,
                    fontSize = 16.sp,
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = precautions,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Thin
                )

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
            subTitle = "サブタイトルはこちらです。以上ですfgsdfgsdfgsdgsdfgsdfgsdfgsdf\n" +
                    "dasdf\n",
            description = "\"秋は紅葉の季節。山々が赤や黄色に染まり、まるで大地が燃えているかのような錯覚を起こします。澄んだ空気の中、落ち葉を踏みしめながらの散歩は、思索を深める絶好の機会となります。冬には雪が静かに降り積もり、世界を白く染め上げます。温かい家族の団欒や、年末年始の伝統行事は、日本人の心のよりどころとなっています。\\n\" +\n" +
                    "                    \"これら四季の移ろいは、日本人の感性や文化に大きな影響を与えてきました。和歌や俳句、絵画や音楽など、多くの芸術作品が四季をテーマに生み出されてきました。また、季節ごとの食材を使った料理や、季節に合わせた着物の柄など、日常生活の中にも四季の感覚が深く根付いています。\\n\" +\n" +
                    "                    \"自然と共に生きる日本人の知恵は、環境への配慮や持続可能な生活様式にもつながっています。四季の変化に敏感であることは、自然の循環を尊重し、その恵みを大切にする心を育みます。現代社会においても、この四季を大切にする心は、忙しい日々の中で立ち止まり、自然とのつながりを感じる貴重な機会を与えてくれるのです。\",\n" +
                    "            description = \"詳細な説明はこちらです。以上です\"",
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
