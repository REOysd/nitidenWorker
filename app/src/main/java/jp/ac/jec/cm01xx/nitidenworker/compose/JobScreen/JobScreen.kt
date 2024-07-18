package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.FirebaseViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JobScreen(
    modifier: Modifier,
    onClickToProfile:() -> Unit,
    firebaseViewModel: FirebaseViewModel
){
    val state = rememberPagerState(
        pageCount = {2},
        initialPage = 0
    )
    val userData by firebaseViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        val uid = firebaseViewModel.auth.currentUser?.uid
        if(uid != null){
            firebaseViewModel.startLeadingUserData(uid)
        }
    }


    Scaffold(
        topBar = {
            JobTopBarContent(state = state)
        }
    ){ innerPadding ->
        HorizontalPager(
            state = state
        ){
            when(it){
                0 -> ClientScreen(
                    modifier = modifier
                        .padding(innerPadding),
                    firebaseViewModel = firebaseViewModel,
                    userData = userData,
                    onClickToProfile = onClickToProfile
                )
                1 -> Page1(modifier = modifier.padding(innerPadding))
            }
        }
    }
}


@Composable
fun Page1(modifier:Modifier){
    Box(modifier = modifier.fillMaxSize()){
        Text(text = "fa")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JobTopBarContent(
    state: PagerState
) {
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val height = systemBarsPadding.calculateTopPadding()
    val scrollPage = listOf("Client","Worker")
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
                text = "MyJob",
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
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[state.currentPage]),
                    height = 3.dp,
                    color = Color(0xFF00B900)  // ここで指定した色を使用
                )
            }
        ) {
            scrollPage.forEachIndexed{ index, PageName ->
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
                        color = if (state.currentPage == index) Color(0xFF00B900) else Color.Gray
                    )
                }
            }
        }
    }
}