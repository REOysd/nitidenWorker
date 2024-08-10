package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen

import android.content.Context
import android.graphics.Bitmap
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.UserDocument
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen.ServiceOfferingData
import jp.ac.jec.cm01xx.nitidenworker.compose.FirebaseViewModel.publishData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ServiceOfferingsDetailViewingScreen(
    startLeadingUserData:(String) -> Unit,
    serviceOfferingData_:StateFlow<publishData?>,
    userData:UserDocument?,
    onClickToPopBackStack:() -> Unit,
    setServiceOfferingData:(ServiceOfferingData?) -> Unit,
    onClickToProfile: () -> Unit,
    createThumbnail: suspend (String?) -> Bitmap?,
    modifier: Modifier
){
    val serviceOfferingData = serviceOfferingData_.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val selectedImageAndMovie = serviceOfferingData.value?.let {
        it.selectImages + it.selectMovies
    }?: emptyList()
    val selectedImageAndMoviePagerState = rememberPagerState(
        pageCount = {selectedImageAndMovie.size},
        initialPage = 0
    )

    LaunchedEffect(key1 = serviceOfferingData.value?.thisUid) {
        serviceOfferingData.value?.thisUid?.let { uid ->
            startLeadingUserData(uid)
        }
    }

    Scaffold(
        topBar = {
            ServiceOfferingsDetailTopBar(
                onClickToPopBackStack = onClickToPopBackStack,
                setServiceOfferingData = setServiceOfferingData
            )
        }
    ){ innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(innerPadding)
        ){

            serviceOfferingData.value?.let{
                ViewingImageAndVideoThumbnail(
                    scope = scope,
                    selectedImageAndMoviePagerState = selectedImageAndMoviePagerState,
                    image = it.selectImages,
                    selectedImageAndMovie = selectedImageAndMovie,
                    selectImageAndMoviePageCount = selectedImageAndMovie.size,
                    createThumbnail = createThumbnail
                )

                TitleAndSubTitleBar(
                    title = it.title,
                    subTitle = it.subTitle,
                    niceCount = it.niceCount,
                    favoriteCount = it.favoriteCount,
                )

                userData?.let{
                    ViewingMyProfileItems(
                        photoUrl = it.userPhoto,
                        email = it.mail,
                        job = it.job,
                        numberOfAchievement = it.numberOfAchievement,
                        totalLikes = it.totalLikes,
                        completionRate = it.completionRate,
                        onClickToProfile = onClickToProfile,
                        context = context,
                    )
                }

                BottomItemBar(
                    category = it.category,
                    deliveryDays = it.deliveryDays,
                    applyingCount = it.applyingCount,
                    checkBoxState = it.checkBoxState,
                    description = it.description,
                    precautions = it.precautions
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewingImageAndVideoThumbnail(
    scope:CoroutineScope,
    selectedImageAndMoviePagerState:PagerState,
    image:List<String?>?,
    selectedImageAndMovie:List<String?>?,
    selectImageAndMoviePageCount:Int,
    createThumbnail:suspend (String?) -> Bitmap?,
){

    Box(
        modifier = Modifier
            .height(170.dp)
            .background(Color.Black)
            .clickable(
                onClick = {}
            )
    ){
        if (selectedImageAndMovie?.isEmpty() == true) {
            Image(
                painter = painterResource(id = R.drawable.nitiiden_icon),
                contentDescription = "defaultImage",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp)
            )

        }else{
            HorizontalPager(
                state = selectedImageAndMoviePagerState,
                modifier = Modifier
                    .background(Color.Black)
                    .height(200.dp)
            ){ page ->
                image?.let {
                    if(it.size <= page){
                        var thumbnail by rememberSaveable { mutableStateOf<Bitmap?>(null) }
                        var isLoading by rememberSaveable { mutableStateOf(true) }

                        selectedImageAndMovie?.let{ selectedMedia ->
                            LaunchedEffect(selectedMedia[page]) {
                                isLoading = true
                                withContext(Dispatchers.IO) {
                                    thumbnail = createThumbnail(selectedImageAndMovie[page])
                                }

                                isLoading = false
                            }

                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                if(isLoading){
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center),
                                        color = Color.Gray
                                    )

                                }else{
                                    thumbnail?.let { _thumbnail ->
                                        Image(
                                            bitmap = _thumbnail.asImageBitmap(),
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
                            }
                        }
                    }else{
                        selectedImageAndMovie?.let{ selectImage ->
                            AsyncImage(
                                model = selectImage[page],
                                contentDescription = "selectImageAndMovie",
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
            ){
                repeat(selectImageAndMoviePageCount){ iteration ->
                    val color = if (selectedImageAndMoviePagerState.currentPage == iteration){
                        Color.DarkGray
                    }else{
                        Color.LightGray
                    }

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
                                        selectedImageAndMoviePagerState
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
fun ViewingMyProfileItems(
    photoUrl:String?,
    email:String?,
    job:String?,
    numberOfAchievement:String?,
    totalLikes:Int?,
    completionRate:String?,
    onClickToProfile:() -> Unit,
    context:Context,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .combinedClickable(
                onClick = {
                    onClickToProfile()
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
    ){
        Spacer(modifier = Modifier.width(12.dp))

        photoUrl?.let {
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
        ){
            email?.let {
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

            job?.let {
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

            numberOfAchievement?.let {
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

            totalLikes.let {
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

            completionRate?.let{
                RatingStar(
                    completionRate = if (it == "--") {
                        0.0f
                    } else {
                        it.toFloat()
                    }
                )
            }
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