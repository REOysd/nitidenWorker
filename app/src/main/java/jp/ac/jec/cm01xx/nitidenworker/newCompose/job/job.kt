package jp.ac.jec.cm01xx.nitidenworker.newCompose.job

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.newCompose.Profile.UserProfile
import jp.ac.jec.cm01xx.nitidenworker.newCompose.common.UserInformationButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JobScreen() {
    val state = rememberPagerState(
        pageCount = {2},
        initialPage = 0
    )
    var isProfileHeight by remember { mutableStateOf(true) }
    val profileHeight = 80.dp
    val animatedHeight by animateDpAsState(
        targetValue = if (isProfileHeight) profileHeight else 0.dp,
        label = "Row height"
    )

    val nestScrollConnection = remember {
        object: NestedScrollConnection {
            private var totalScroll = 0f
            private val scrollThreshold = 50f

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                totalScroll += delta

                if(totalScroll < -scrollThreshold && isProfileHeight){
                    isProfileHeight = false
                    totalScroll = 0f
                }else if(totalScroll > scrollThreshold && !isProfileHeight){
                    isProfileHeight = true
                    totalScroll = 0f
                }
                return Offset.Zero
            }
        }
    }

    Scaffold(
        topBar = { JobTopBarContent(state = state) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
        ) {
            OfferContent(
                animatedHeight = animatedHeight,
                userName = "ユーザーネーム",
                studentID = "24cm0137@jec.ac.jp"
            )
            HorizontalPager(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(y = animatedHeight + profileHeight)
                    .nestedScroll(nestScrollConnection)
            ) {
                when (it) {
                    0 -> Offer()
                    1 -> UserProfile()
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
    val systemBarColors = rememberSystemUiController()
    SideEffect {
        systemBarColors.setStatusBarColor(
            color = Color.White,
            darkIcons = true
        )
    }

    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val height = systemBarsPadding.calculateTopPadding()
    val scrollPage = listOf(
        stringResource(id = R.string.jobScreen_offer),
        stringResource(id = R.string.jobScreen_submit),
    )
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(top = height)
            .height(90.dp)
            .fillMaxWidth()
    ){
        TabRow(
            selectedTabIndex = state.currentPage,
            modifier = Modifier
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
                        .height(50.dp)
                        .background(Color.White)
                ) {
                    Text(
                        text = scrollPage[index],
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (state.currentPage == index) colorResource(id = R.color.bottomNavigationBarColor)
                        else Color.Gray,
                        modifier = Modifier
                            .padding(top = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OfferContent(
    animatedHeight: Dp,
    userName:String,
    studentID:String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color(0xFFD3D0D0),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2.dp.toPx()
                )
            }
    ) {
        UserInformationButton(
            userName = "ユーザーネーム",
            studentID = "24cm0137@jec.ac.jp",
            height = animatedHeight
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.White)
        ) {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = Color(0xFF45C152),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 80.dp)
                    .height(50.dp)
                    .align(Alignment.Center)
            ) {
                Text(
                    text = stringResource(id = R.string.jobScreen_OfferProduction),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.hiragino_medium)),
                        fontSize = 16.sp,
                        color = Color.White,
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreViewJob() {
    JobScreen()
}