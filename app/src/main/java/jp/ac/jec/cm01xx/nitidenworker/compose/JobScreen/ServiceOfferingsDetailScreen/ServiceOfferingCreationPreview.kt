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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.FirebaseUser
import jp.ac.jec.cm01xx.nitidenworker.compose.FirebaseViewModel.FirebaseViewModel
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.ServiceOfferingData
import jp.ac.jec.cm01xx.nitidenworker.UserDocument
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.serviceOfferingCreateScreen.VideoThumbnail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServiceOfferingCreationPreview(
    serviceOfferingsDetailViewModel: ServiceOfferingsDetailViewModel = viewModel(),
    firebaseViewModel: FirebaseViewModel,
    data: ServiceOfferingData,
    onClickToPopBackStack:() -> Unit,
    setServiceOfferingData:(ServiceOfferingData?) -> Unit,
    modifier: Modifier,
){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState by serviceOfferingsDetailViewModel.uiState.collectAsState()
    val selectImageAndMovie by serviceOfferingsDetailViewModel.selectImageAndMovie.collectAsState()
    val selectImageAndMoviePagerState = rememberPagerState(
        pageCount = {uiState.selectImageAndMoviePageCount},
        initialPage = 0
    )
    val currentUser = firebaseViewModel.auth.currentUser
    val userData by firebaseViewModel.userData.collectAsState()
    
    LaunchedEffect(data) {
        serviceOfferingsDetailViewModel.initializeData(data)
    }

    Scaffold(
        topBar = {
            ServiceOfferingCreationPreviewTopBar(
                onClickToPopBackStack = onClickToPopBackStack,
                setServiceOfferingData = setServiceOfferingData
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
                selectImageAndMoviePageCount = uiState.selectImageAndMoviePageCount,
                changeIsShowSelectedImageAndMovieDialog = serviceOfferingsDetailViewModel::changeIsShowSelectedImageAndMovieDialog,
                changeImageAndMovieIndex = serviceOfferingsDetailViewModel::changeImageAndMovieIndex
            )

            TitleAndSubTitleBar(
                uid = currentUser?.uid,
                serviceUid = userData?.uid,
                title = uiState.title,
                subTitle = uiState.subTitle,
                niceCount = 0,
                favoriteCount = 0,

            )

            MyProfileItems(
                currentUser = currentUser,
                context = context,
                userData = userData
            )


            BottomItemBar(
                category = data.category,
                deliveryDays = data.deliveryDays,
                applyingCount = data.applyingCount,
                checkBoxState = data.checkBoxState,
                description = data.description,
                precautions = data.precautions ?: "",
            )

            if(uiState.isShowImageDialog){
                SelectedImageAndMovieDialog(
                    changeIsShowSelectedImageAndMovieDialog = {
                        serviceOfferingsDetailViewModel.changeIsShowSelectedImageAndMovieDialog(it)
                    },
                    images = uiState.images,
                    selectedImageAndMovie = selectImageAndMovie,
                    imageAndMovieIndex = uiState.imageAndMovieIndex
                )
            }
        }
    }
}


@Composable
fun TitleAndSubTitleBar(
    uid:String?,
    serviceUid: String?,
    title:String,
    subTitle:String,
    niceCount:Int,
    favoriteCount:Int,
    likedUsers:List<String?>? = emptyList(),
    favoriteUsers: List<String?>? = emptyList(),
    updateLikedUsers: () -> Unit = {},
    updateFavoriteUsers: () -> Unit = {},
    onClickHeartIcon: (Boolean) -> Unit = {},
    onClickFavoriteIcon: (Boolean) -> Unit = {},
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
            ) {
                HeartIcon(
                    uid = uid,
                    serviceUid = serviceUid,
                    likedUsers = likedUsers,
                    updateLikedUsers = updateLikedUsers,
                    onChangeNiceCount = {
                        onClickHeartIcon(it)
                    },
                )

                Text(
                    text = niceCount.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )

                FavoriteIcon(
                    uid = uid,
                    serviceUid = serviceUid,
                    favoriteUsers = favoriteUsers,
                    updateFavoriteUsers = updateFavoriteUsers,
                    onChangeFavoriteCount = {
                        onClickFavoriteIcon(it)
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
                            tint = colorResource(id = R.color.RatingStarYellow),
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
            text = "（${stringResource(id = R.string.UserProfileScreen_completionRate)}:${completionRate}）",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(start = 4.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun HeartIcon(
    uid:String?,
    serviceUid: String?,
    likedUsers:List<String?>?,
    updateLikedUsers:() -> Unit,
    onChangeNiceCount:(Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    val scope = rememberCoroutineScope()
    val heart by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.heart_lottie))
    var isLiked by  remember { mutableStateOf(false) }
    var isProgress by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (isLiked) 0.6f else 0f,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    // ここでアイコンタップの誤作動が起きてる（イイね回数がおかしくなる）
    // 引数をlikedUsers以外にすれば治るが、反映が遅くなる
    LaunchedEffect (likedUsers) {
        isLiked = uid != null && likedUsers?.contains(uid) == true
    }

    IconButton(
        onClick = {
            if(uid != serviceUid && !isProgress){
                isProgress = true
                isLiked = if(isLiked){
                    false
                }else{
                    true
                }

                scope.launch{
                    try {
                        onChangeNiceCount(isLiked)
                        updateLikedUsers()
                    } catch (e: Exception) {
                        isLiked = !isLiked
                        Log.d("FavoriteError", e.message.toString())
                    } finally {
                        isProgress = false
                        Log.d("isProgress", isLiked.toString())
                    }
                }
            }
        },
        modifier = modifier.size(50.dp)
    ) {
        LottieAnimation(
            composition = heart,
            progress = { animatedProgress }
        )
    }
}


@Composable
fun FavoriteIcon(
    uid:String?,
    serviceUid: String?,
    favoriteUsers:List<String?>?,
    updateFavoriteUsers:() -> Unit,
    onChangeFavoriteCount:(Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val heart by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.favorite_lottie))
    var isFavorite by remember { mutableStateOf(false) }
    var isProgress by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(
        targetValue = if (isFavorite) 1f else 0f,
        animationSpec = tween(durationMillis = 900), label = ""
    )

    // ここでアイコンタップの誤作動が起きてる（お気に入り回数がおかしくなる）
    // 引数をfavoriteUsers以外にすれば治るが、反映が遅くなる
    LaunchedEffect(favoriteUsers) {
        isFavorite = uid != null && favoriteUsers != null && favoriteUsers.contains(uid)
    }

    IconButton(
        onClick = {
            if(uid != serviceUid && !isProgress){
                isProgress = true
                isFavorite = if(isFavorite){
                    false
                }else{
                    true
                }

                scope.launch{
                    try {
                        onChangeFavoriteCount(isFavorite)
                        updateFavoriteUsers()
                    } catch (e: Exception) {
                        isFavorite = !isFavorite
                        Log.d("FavoriteError", e.message.toString())
                    } finally {
                        isProgress = false
                        Log.d("isProgress", isFavorite.toString())
                    }
                }
            }
        },
        modifier = modifier.size(50.dp),
        enabled = !isProgress
    ) {
        LottieAnimation(
            composition = heart,
            progress = { animatedProgress }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageAndVideoThumbnail(
    context: Context,
    scope:CoroutineScope,
    selectImageAndMoviePagerState:PagerState,
    images:List<Uri?>,
    selectImageAndMovie:List<Uri?>,
    selectImageAndMoviePageCount:Int,
    changeIsShowSelectedImageAndMovieDialog: (Boolean) -> Unit,
    changeImageAndMovieIndex:(Int) -> Unit,
    ) {

    Box(
        modifier = Modifier
            .background(Color.Black)
            .height(170.dp),
    ){
        if (selectImageAndMovie.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(Color.Black),
                onClick = {
                    changeIsShowSelectedImageAndMovieDialog(true)
                }
            ){
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.nitiiden_icon),
                        contentDescription = stringResource(id = R.string.ViewingImage_default_description),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp, end = 10.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.ViewingImage_default_text),
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    )
                }
            }
        } else {
            HorizontalPager(
                state = selectImageAndMoviePagerState,
                modifier = Modifier
                    .background(Color.Black)
                    .height(200.dp)
            ) { page ->

                images.let {
                    if (it.size <= page) {
                        var thumbnail by rememberSaveable { mutableStateOf<Bitmap?>(null) }

                        LaunchedEffect(selectImageAndMovie[page]) {
                            withContext(Dispatchers.IO) {
                                try {
                                    val uri = Uri.parse(selectImageAndMovie[page].toString())
                                    context.contentResolver.openFileDescriptor(uri, "r")
                                        ?.use { pfd ->
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
                            Card(
                                modifier = Modifier
                                    .fillMaxSize(),
                                shape = RectangleShape,
                                colors = CardDefaults.cardColors(Color.Black),
                                onClick = {
                                    changeIsShowSelectedImageAndMovieDialog(true)
                                    changeImageAndMovieIndex(page)
                                }
                            ){
                                Box(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )

                                    IconButton(
                                        onClick = {
                                            changeIsShowSelectedImageAndMovieDialog(true)
                                            changeImageAndMovieIndex(page)
                                        },
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .size(50.dp),
                                        colors = IconButtonDefaults.iconButtonColors(Color.White)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = stringResource(
                                                id = R.string.ViewingVideo_playVideoIcon_description
                                            ),
                                            modifier = Modifier
                                                .size(40.dp),
                                            tint = Color.Black
                                        )
                                    }
                                }
                            }
                        }

                    } else {
                        Card(
                            modifier = Modifier
                                .fillMaxSize(),
                            shape = RectangleShape,
                            colors = CardDefaults.cardColors(Color.Black),
                            onClick = {
                                changeIsShowSelectedImageAndMovieDialog(true)
                                changeImageAndMovieIndex(page)
                            }
                        ){
                            AsyncImage(
                                model = selectImageAndMovie[page],
                                contentDescription = stringResource(id = R.string.SelectedImageAndMovie_description),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
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
private fun MyProfileItems(
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
                contentDescription = stringResource(id = R.string.ProfileImage_description),
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
                    text = "${stringResource(id = R.string.UserProfileScreen_department)} : $it",
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
                    text = "${stringResource(id = R.string.UserProfileScreen_achievements)} : $it",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))


            userData?.totalLikes?.let {
                Text(
                    text = "${stringResource(id = R.string.Apply)} : $it",
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
                contentDescription = stringResource(id = R.string.ArrowBack_Icon_description),
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
    applyingCount:Int,
    checkBoxState:Boolean,
    description:String,
    precautions:String?,
){
    Column {
        Spacer(modifier = Modifier.height(20.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 20.dp)
        ){
            Text(
                text = stringResource(id = R.string.category),
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
                color = colorResource(id = R.color.nitidenGreen),
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .border(
                        color = colorResource(id = R.color.nitidenGreen),
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
                text = stringResource(id = R.string.NumberOfApplying),
                fontWeight = FontWeight.W500,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "$applyingCount",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )

            Text(
                text = "　${stringResource(id = R.string.NumberOfPeople)}",
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
                text = stringResource(id = R.string.deliveryDays),
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
                text = "　${stringResource(id = R.string.deliveryDays_day)}",
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
                text = stringResource(id = R.string.ServiceOfferingCreationScreen_videoChat),
                fontWeight = FontWeight.W500,
                fontSize = 13.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = if(checkBoxState) stringResource(id = R.string.Yes) else stringResource(id = R.string.No),
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
                text = stringResource(id = R.string.ServiceOfferingCreationScreen_descriptionOfServices),
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

        if(precautions?.isNotEmpty() == true){
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.ServiceOfferingCreationScreen_precautions),
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

@Composable
fun SelectedImageAndMovieDialog(
    changeIsShowSelectedImageAndMovieDialog:(Boolean) -> Unit,
    images:List<Uri?>,
    selectedImageAndMovie:List<Uri?>,
    imageAndMovieIndex:Int
){
    Dialog(
        onDismissRequest = { changeIsShowSelectedImageAndMovieDialog(false) }
    ){
        if (selectedImageAndMovie.isEmpty()){
            Image(
                painter = painterResource(id = R.drawable.nitiiden_icon),
                contentDescription = stringResource(id = R.string.ViewingImage_default_description),
                contentScale = ContentScale.Fit,
            )
        }else if(images.size <= imageAndMovieIndex){
            selectedImageAndMovie[imageAndMovieIndex]?.let{
                VideoThumbnail(it)
            }
        }else{
            AsyncImage(
                model = selectedImageAndMovie[imageAndMovieIndex],
                contentDescription = stringResource(id = R.string.SelectedImageAndMovie_description),
                contentScale = ContentScale.Fit,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ServiceOfferingsDetailScreenPreview(){
    ServiceOfferingCreationPreview(
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
            checkBoxState = false,
            niceCount = 10,
            favoriteCount = 10,
            applyingCount = 100,
        ),
        onClickToPopBackStack = { /*TODO*/ },
        modifier = Modifier,
        firebaseViewModel = FirebaseViewModel(),
        setServiceOfferingData = {}
    )
}
