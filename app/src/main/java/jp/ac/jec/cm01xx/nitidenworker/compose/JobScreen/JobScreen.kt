package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.PublishData
import jp.ac.jec.cm01xx.nitidenworker.UserDocument
import jp.ac.jec.cm01xx.nitidenworker.compose.ApplyScreen
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JobScreen(
    modifier: Modifier,
    userData:UserDocument?,
    auth_:FirebaseAuth?,
    startLeadingUserData:(String) -> Unit,
    myServiceOfferings:StateFlow<List<PublishData?>>,
    ApplyingServiceOfferings:StateFlow<List<PublishData?>>,
    onClickToProfile:() -> Unit,
    onClickToServiceOfferingsScreen:() -> Unit,
    onClickToRequestServiceScreen:() -> Unit,
    onClickToServiceOfferingDetailScreen:() -> Unit,
    getServiceOfferingData:(String) -> Unit,
    getMyServiceOfferings:() -> Unit,
    getApplyingServiceOfferings:() -> Unit,
    updateLikedUsers:(String,String) -> Unit,
    updateFavoriteUsers:(String,String) -> Unit,
    onClickHeartAndFavoriteIcon:(String, Boolean, String) -> Unit,
    cleanServiceOfferingData:() -> Unit,
    cleanServiceOfferingCreationPreview:() -> Unit
){
    val state = rememberPagerState(
        pageCount = {3},
        initialPage = 0
    )
    val currentUser = auth_?.currentUser
    val context = LocalContext.current
    var isProfileLinkVisible by remember{ mutableStateOf(true) }
    val profileLinkHeight = 170.dp
    val animatedHeight by animateDpAsState(
        targetValue = if (isProfileLinkVisible) profileLinkHeight else 0.dp,
        label = "Profile link height"
    )
    var lastScrollOffset by remember { mutableFloatStateOf(0f) }
    val nestScrollConnection = remember {
        object: NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if(delta < 0f){
                    isProfileLinkVisible = false
                }else if(delta > 0f ){
                    isProfileLinkVisible = true
                }
                lastScrollOffset += delta
                return Offset.Zero
            }
        }
    }

//    LaunchedEffect(Unit) {
//        val uid = auth_?.currentUser?.uid
//        if(uid != null){
//            startLeadingUserData(uid)
//        }
//    }


    Scaffold(
        topBar = {
            JobTopBarContent(state = state)
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ){
            androidx.compose.animation.AnimatedVisibility(
                visible = isProfileLinkVisible,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ) {
                Column(
                    modifier = Modifier
                        .height(170.dp)
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .height(90.dp)
                            .combinedClickable(
                                onClick = {
                                    cleanServiceOfferingData()
                                    onClickToProfile()
                                }
                            )
                    ) {
                        Row(
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
                        ) {
                            Spacer(modifier = Modifier.width(12.dp))

                            currentUser?.photoUrl?.let {
                                AsyncImage(
                                    model = ImageRequest.Builder(context = context)
                                        .data(it)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = stringResource(id = R.string.UserPhoto_description),
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
                            ) {
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
                                        text = it,
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .height(80.dp)
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
                        Spacer(modifier = Modifier.weight(1f))

                        FloatingActionButton(
                            onClick = onClickToServiceOfferingsScreen,
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .width(170.dp)
                                .height(50.dp),
                            containerColor = colorResource(id = R.color.nitidenGreen),

                            ) {
                            Text(
                                text = stringResource(id = R.string.ServiceOfferings),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        FloatingActionButton(
                            onClick = {onClickToRequestServiceScreen()},
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .width(170.dp)
                                .height(50.dp),
                            containerColor = Color(0xFF47c6c6)
                        ) {
                            Text(
                                text = stringResource(id = R.string.ServiceRequest),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            HorizontalPager(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(y = animatedHeight)
            ) {
                when (it) {
                    0 -> RequestServiceScreen(
                        uid = currentUser?.uid,
                        myServiceOfferings = myServiceOfferings,
                        getMyServiceOfferings = getMyServiceOfferings,
                        getServiceOfferingData = getServiceOfferingData,
                        cleanServiceOfferingCreationPreview = cleanServiceOfferingCreationPreview,
                        onClickToServiceOfferingDetailScreen = onClickToServiceOfferingDetailScreen,
                        updateLikedUsers = updateLikedUsers,
                        updateFavoriteUsers = updateFavoriteUsers,
                        onClickHeartAndFavoriteIcon = onClickHeartAndFavoriteIcon,
                        modifier = modifier
                            .nestedScroll(nestScrollConnection),
                    )

                    1 -> ClientScreen(
                        modifier = modifier
                            .nestedScroll(nestScrollConnection)
                    )

                    2 -> ApplyScreen(
                        uid = currentUser?.uid,
                        ApplyingServiceOfferings = ApplyingServiceOfferings,
                        getServiceOfferingData = getServiceOfferingData,
                        getApplyingServiceOfferings = getApplyingServiceOfferings,
                        cleanServiceOfferingCreationPreview = cleanServiceOfferingCreationPreview,
                        onClickToServiceOfferingDetailScreen = onClickToServiceOfferingDetailScreen,
                        updateLikedUsers = updateLikedUsers,
                        updateFavoriteUsers = updateFavoriteUsers,
                        onClickHeartAndFavoriteIcon = onClickHeartAndFavoriteIcon,
                        modifier = modifier
                            .nestedScroll(nestScrollConnection)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JobTopBarContent(
    state: PagerState
) {
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val height = systemBarsPadding.calculateTopPadding()
    val scrollPage = listOf(
        stringResource(id = R.string.ScrollPage_worker),
        stringResource(id = R.string.ScrollPage_client),
        stringResource(id = R.string.ScrollPage_apply)
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(top = height)
            .height(90.dp)
            .fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Text(
                text = stringResource(id = R.string.JobTopBarContent_title),
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.Center),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.W500,
            )
        }

        TabRow(
            selectedTabIndex = state.currentPage,
            modifier = Modifier
                .height(30.dp)
                .background(Color.White)
                .fillMaxWidth(),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[state.currentPage]),
                    height = 3.dp,
                    color = colorResource(id = R.color.bottomNavigationBarColor)
                )
            }
        ) {
            scrollPage.forEachIndexed{ index,_ ->
                Tab(
                    selected = index == state.currentPage,
                    onClick = {
                        scope.launch {
                            state.animateScrollToPage(index)
                        }
                    },
                    modifier = Modifier
                        .height(30.dp)
                        .background(Color.White)
                ) {
                    Text(
                        text = scrollPage[index],
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (state.currentPage == index) colorResource(id = R.color.bottomNavigationBarColor)
                        else Color.Gray
                    )
                }
            }
        }
    }
}