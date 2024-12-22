package jp.ac.jec.cm01xx.nitidenworker.newCompose.Profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileHeader(
    state: PagerState,
    scope: CoroutineScope
) {
    val scrollPage = listOf(
        stringResource(id = R.string.UserScreen_Profile),
        "設定"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
                .background(Color.LightGray)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "ユーザーネーム",
            style = TextStyle(
                fontSize = 23.sp,
                fontFamily = FontFamily(Font(R.font.hiragino_black))
            )
        )
        Text(
            text = "studentID : 24cm0137@gmail.com",
            style = TextStyle(
                fontSize = 13.sp,
                fontFamily = FontFamily(Font(R.font.hiragino_medium))
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        TabRow(
            selectedTabIndex = state.currentPage,
            modifier = Modifier
                .height(40.dp)
                .align(Alignment.CenterHorizontally)
                .background(Color.White)
                .fillMaxWidth(),
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[state.currentPage]),
                    height = 3.dp,
                    color = colorResource(id = R.color.bottomNavigationBarColor)
                )
            },
            divider = {
                HorizontalDivider(
                    thickness = 3.dp,
                    color = Color.LightGray
                )
            }
        ) {
            scrollPage.forEachIndexed { index, s ->
                Tab(
                    selected = index == state.currentPage,
                    onClick = {
                        scope.launch {
                            state.animateScrollToPage(index)
                        }
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .background(Color.White),
                ) {
                    Text(
                        text = scrollPage[index],
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.hiragino_black)),
                        fontSize = 14.sp,
                        color =
                        if(index == state.currentPage) colorResource(id = R.color.bottomNavigationBarColor)
                        else Color.Gray
                    )
                }
            }
        }
    }
}

//fun ProfileTopBar(
//    height: Dp,
//    context: Context,
//    userData: UserDocument?,
//    state: PagerState,
//    scrollPage:List<String>,
//    scope: CoroutineScope
//){
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = height)
//            .height(230.dp)
//            .background(Color.White)
//            .verticalScroll(rememberScrollState())
//    ) {
//        userData?.userPhoto?.let{
//            Spacer(modifier = Modifier.height(12.dp))
//            AsyncImage(
//                model = ImageRequest.Builder(context = context)
//                    .data(it)
//                    .crossfade(true)
//                    .build(),
//                contentDescription = stringResource(id = R.string.UserPhoto_description),
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally)
//                    .size(100.dp)
//                    .clip(RoundedCornerShape(80.dp))
//            )
//        }
//
//        userData?.name?.let{
//            Spacer(modifier = Modifier.height(12.dp))
//            Text(
//                text = it,
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally),
//                fontWeight = FontWeight(1000),
//                fontSize = 20.sp
//            )
//        }
//
//        userData?.uid?.let{
//            Spacer(modifier = Modifier.height((8.dp)))
//            Text(
//                text = "ID:${it}",
//                modifier = Modifier
//                    .align(Alignment.CenterHorizontally),
//            )
//        }
//        Spacer(modifier = Modifier.weight(1f))
//
//        TabRow(
//            selectedTabIndex = state.currentPage,
//            modifier = Modifier
//                .height(40.dp)
//                .align(Alignment.CenterHorizontally)
//                .background(Color.Black)
//                .fillMaxWidth(),
//            indicator = { tabPositions ->
//                TabRowDefaults.SecondaryIndicator(
//                    modifier = Modifier.tabIndicatorOffset(tabPositions[state.currentPage]),
//                    height = 3.dp,
//                    color = colorResource(id = R.color.bottomNavigationBarColor)
//                )
//            },
//        ) {
//            scrollPage.forEachIndexed { index, s ->
//                Tab(
//                    selected = index == state.currentPage,
//                    onClick = {
//                        scope.launch {
//                            state.animateScrollToPage(index)
//                        }
//                    },
//                    modifier = Modifier
//                        .height(40.dp)
//                        .background(Color.White),
//                ) {
//                    Text(
//                        text = scrollPage[index],
//                        textAlign = TextAlign.Center,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 14.sp,
//                        color =
//                        if(index == state.currentPage) colorResource(id = R.color.bottomNavigationBarColor)
//                        else Color.Gray
//                    )
//                }
//            }
//        }
//    }
//}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    val state = rememberPagerState (
        pageCount = {2},
        initialPage = 0
    )
    val scope = rememberCoroutineScope()
    ProfileHeader(
        state = state,
        scope = scope
    )
}