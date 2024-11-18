package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.ServiceOfferingData
import jp.ac.jec.cm01xx.nitidenworker.PublishData
import jp.ac.jec.cm01xx.nitidenworker.UserDocument
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.serviceOfferingCreateScreen.VideoThumbnail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@Composable
fun ServiceOfferingsDetailViewingScreen(
    uid:String?,
    userDocument:UserDocument?,
    serviceOfferingData:PublishData?,
    viewModel:ServiceOfferingsDetailViewingViewModel = viewModel(),
    startLeadingUserData:(String) -> Unit,
    onClickToPopBackStack:() -> Unit,
    setServiceOfferingData:(ServiceOfferingData?) -> Unit,
    onClickToProfile: () -> Unit,
    onClickToApplicantScreen: () -> Unit,
    createThumbnail: suspend (String?) -> Bitmap?,
    updateLikedAndFavoriteUsers:(String,String) -> Unit,
    addApplicant:(String,String) -> Unit,
    onClickHeartAndFavoriteIcon:(String, Boolean, String) -> Unit,
    modifier: Modifier
){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()


    LaunchedEffect(userDocument,serviceOfferingData) {
        viewModel.initializeData(userDocument, serviceOfferingData)
        serviceOfferingData?.thisUid?.let { uid -> startLeadingUserData(uid) }
    }

    Scaffold(
        topBar = {
            ServiceOfferingCreationPreviewTopBar(
                onClickToPopBackStack = onClickToPopBackStack,
                setServiceOfferingData = setServiceOfferingData
            )
        },

        bottomBar = {
            if (uid != serviceOfferingData?.thisUid) {
                NavigateFloatingActionButtonOnViewing(
                    myUid = uid,
                    uiState = uiState,
                    changeConfirmDialog = { viewModel.changeIsShowConfirmDialog(it) },
                )
            }else{
                NavigateFloatingActionButtonOnMyViewing(
                    onClickToApplicantScreen = onClickToApplicantScreen
                )
            }
        }
    ) { innerPadding ->

        ServiceOfferingsDetailViewingContent(
            uid = uid,
            scope = scope,
            context = context,
            uiState = uiState,
            viewModel = viewModel,
            onClickToProfile = onClickToProfile,
            createThumbnail = createThumbnail,
            updateLikedAndFavoriteUsers = updateLikedAndFavoriteUsers,
            onClickHeartAndFavoriteIcon = onClickHeartAndFavoriteIcon,
            modifier = modifier.padding(innerPadding)
        )

        if (uiState.isShowConfirmDialog) {
            ConfirmDialog(
                id = uiState.serviceOfferingData?.id,
                onDismiss = {
                    viewModel.changeIsShowConfirmDialog(false)
                },
                onConfirm = {
                    viewModel.changeIsShowConfirmDialog(false)
                },
                addApplicant = addApplicant,
                changeIsApplied = viewModel::changeIsApplied
            )
        }

        if (uiState.isShowSelectedImageAndMovieDialog) {
            SelectedImageAndMovieDialog(
                images = uiState.serviceOfferingData?.selectImages,
                imageAndMovieIndex = uiState.imageAndMovieIndex,
                selectedImageAndMovie = uiState.selectedImageAndMovie,
                onDismiss = {
                    viewModel.changeIsShowSelectedImageAndMovieDialog(false)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ServiceOfferingsDetailViewingContent(
    uid:String?,
    scope: CoroutineScope,
    uiState: ServiceOfferingsDetailViewingUiState,
    context:Context,
    viewModel: ServiceOfferingsDetailViewingViewModel,
    onClickToProfile: () -> Unit,
    createThumbnail: suspend (String?) -> Bitmap?,
    updateLikedAndFavoriteUsers:(String,String) -> Unit,
    onClickHeartAndFavoriteIcon:(String, Boolean, String) -> Unit,
    modifier: Modifier
) {
    val selectedImageAndMoviePagerState = rememberPagerState(
        pageCount = {uiState.selectedImageAndMovie.size},
        initialPage = 0
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        uiState.serviceOfferingData?.let { offeringData ->
            ViewingImageAndVideoThumbnail(
                scope = scope,
                selectedImageAndMoviePagerState = selectedImageAndMoviePagerState,
                image = offeringData.selectImages,
                selectedImageAndMovie = uiState.selectedImageAndMovie,
                selectImageAndMoviePageCount = uiState.selectedImageAndMovie.size,
                createThumbnail = createThumbnail,
                changeIsShowSelectedImageAndMovieDialog =
                { viewModel.changeIsShowSelectedImageAndMovieDialog(it) },
                changeImageAndMovieIndex = viewModel::changeImageAndMovieIndex
            )

            TitleAndSubTitleBar(
                uid = uid,
                serviceUid = offeringData.thisUid,
                title = offeringData.title,
                subTitle = offeringData.subTitle,
                niceCount = offeringData.niceCount,
                favoriteCount = offeringData.favoriteCount,
                likedUsers = offeringData.likedUserIds,
                favoriteUsers = offeringData.favoriteUserIds,
                updateLikedUsers = { updateLikedAndFavoriteUsers(offeringData.id, "likedUserIds") },
                updateFavoriteUsers = { updateLikedAndFavoriteUsers(offeringData.id, "favoriteUserIds") },
                onClickHeartIcon = { onClickHeartAndFavoriteIcon("niceCount", it, offeringData.id) },
                onClickFavoriteIcon = { onClickHeartAndFavoriteIcon("favoriteCount", it, offeringData.id) },
            )

            uiState.userDocument?.let {
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
                category = offeringData.category,
                deliveryDays = offeringData.deliveryDays,
                applyingCount = offeringData.applyingCount,
                checkBoxState = offeringData.checkBoxState,
                description = offeringData.description,
                precautions = offeringData.precautions
            )

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
    changeIsShowSelectedImageAndMovieDialog:(Boolean) -> Unit,
    changeImageAndMovieIndex:(Int) -> Unit
){

    Box(
        modifier = Modifier
            .height(170.dp)
            .background(Color.Black)
    ){
        if (selectedImageAndMovie?.isEmpty() == true) {
            Card(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(Color.Black),
                onClick = {
                    changeIsShowSelectedImageAndMovieDialog(true)
                }
            ){
                Image(
                    painter = painterResource(id = R.drawable.nitiiden_icon),
                    contentDescription = stringResource(id = R.string.ViewingImage_default_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp, end = 10.dp)
                )
            }

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
                                    Card(
                                        modifier = Modifier
                                        .fillMaxSize(),
                                        shape = RectangleShape,
                                        colors = CardDefaults.cardColors(Color.Black),
                                        onClick = {
                                            changeIsShowSelectedImageAndMovieDialog(true)
                                            changeImageAndMovieIndex(page)
                                        }
                                    ) {
                                        Box{
                                            thumbnail?.let { thumbnail ->
                                                Image(
                                                    bitmap = thumbnail.asImageBitmap(),
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
                                                    colors = IconButtonDefaults.iconButtonColors(
                                                        Color.White
                                                    )
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
                                }
                            }
                        }
                    }else{
                        selectedImageAndMovie?.let{ selectImage ->
                            Card(
                                modifier = Modifier
                                .fillMaxSize(),
                                shape = RectangleShape,
                                colors = CardDefaults.cardColors(Color.Black),
                                onClick = {
                                    changeIsShowSelectedImageAndMovieDialog(true)
                                    changeImageAndMovieIndex(page)
                                }
                            ) {
                                AsyncImage(
                                    model = selectImage[page],
                                    contentDescription = stringResource(
                                        id = R.string.SelectedImageAndMovie_description
                                    ),
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
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
                contentDescription = stringResource(
                    id = R.string.ProfileImage_description
                ),
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
                    text = "${stringResource(id = R.string.UserProfileScreen_department)} : $it",
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
                    text = "${stringResource(id = R.string.UserProfileScreen_achievements)} : $it",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            totalLikes?.let {
                Text(
                    text = "${stringResource(id = R.string.TotalLikes)} : $it",
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
                contentDescription = stringResource(id = R.string.KeyboardArrowRight_icon_description),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp)
            )
        }
    }
}

@Composable
fun NavigateFloatingActionButtonOnViewing(
    myUid:String?,
    uiState: ServiceOfferingsDetailViewingUiState,
    changeConfirmDialog:(Boolean) -> Unit,
){

    uiState.serviceOfferingData?.let{ data ->
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        if(data.applicant.contains(myUid) || uiState.isApplied) {

                        }else{
                            changeConfirmDialog(true)
                        }
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(53.dp)
                        .padding(horizontal = 8.dp),
                    containerColor = if(data.applicant.contains(myUid) || uiState.isApplied){
                        Color.Gray
                    }else {
                        colorResource(id = R.color.nitidenGreen)
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.Apply),
                        fontWeight = FontWeight.ExtraBold,
                        color = if(data.applicant.contains(myUid) || uiState.isApplied){
                            Color.LightGray
                        }else{
                            Color.White
                        },
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(bottom = 8.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(53.dp)
                        .padding(horizontal = 8.dp),
                    containerColor = colorResource(id = R.color.nitidenBlue),
                ) {
                    Text(
                        text = stringResource(id = R.string.ListenToTheStory),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun NavigateFloatingActionButtonOnMyViewing(
    onClickToApplicantScreen:() -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(top = 20.dp)
            .background(Color.White)
    ){
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 8.dp)
                .background(Color.White)
        ) {
            FloatingActionButton(
                onClick = {
                    onClickToApplicantScreen()
                },
                modifier = Modifier
                    .width(300.dp)
                    .height(53.dp)
                    .padding(horizontal = 8.dp),
                containerColor = colorResource(id = R.color.applyButtonColor),
            ) {
                Text(
                    text = stringResource(id = R.string.ConfirmApplicant),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
fun ConfirmDialog(
    id:String?,
    addApplicant: (String, String) -> Unit,
    changeIsApplied:(Boolean) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    var showConfirmation by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.IconDescriptionOnConfirmDialog),
                    modifier = Modifier
                        .size(64.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(id = R.string.TitleOnConfirmDialog),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.SubTextOnConfirmDialog),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                            ),
                        modifier = Modifier
                            .width(88.dp)
                            .height(52.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.cancelButtonTextOnConfirmDialog),
                            color = Color.LightGray,
                            fontSize = 17.sp
                            )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = {
                            scope.launch {
                                showConfirmation = true
                                delay(600)
                                onConfirm()
                                showConfirmation = false
                                id?.let{ addApplicant(it,"applicant") }
                                changeIsApplied(true)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.nitidenGreen)
                        ),
                        modifier = Modifier
                            .width(88.dp)
                            .height(52.dp)
                    ) {
                        AnimatedContent(
                            targetState = showConfirmation, label = ""
                        ) { isConfirmed ->
                            if (isConfirmed) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Confirmed",
                                    tint = Color.White
                                )
                            } else {
                                Text(
                                    stringResource(id = R.string.confirmButtonTextOnConfirmDialog),
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun SelectedImageAndMovieDialog(
    images:List<String?>?,
    imageAndMovieIndex:Int,
    selectedImageAndMovie: List<String?>,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        when  {
            selectedImageAndMovie.isEmpty() -> {
                val painter = painterResource(id = R.drawable.nitiiden_icon)
                val zoomState = rememberZoomState(contentSize = painter.intrinsicSize)

                Image(
                    painter = painter,
                    contentDescription = stringResource(id = R.string.ViewingImage_default_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(300.dp)
                        .zoomable(zoomState),
                )
            }
           images == null || imageAndMovieIndex >= images.size -> {
                selectedImageAndMovie[imageAndMovieIndex]?.let{
                    val convertUri: Uri =Uri.parse(it)
                    VideoThumbnail(convertUri)
                }
            }
            else -> {
                val zoomState = rememberZoomState()

                AsyncImage(
                    model = selectedImageAndMovie[imageAndMovieIndex],
                    contentDescription = stringResource(id = R.string.SelectedImageAndMovie_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .defaultMinSize(300.dp)
                        .zoomable(zoomState),
                )
            }
        }
    }
}