package jp.ac.jec.cm01xx.nitidenworker.compose.UserScreenFile

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseUser
import jp.ac.jec.cm01xx.nitidenworker.FirebaseViewModel
import jp.ac.jec.cm01xx.nitidenworker.compose.UserScreenFile.UserProfileAppeal.UserProfileAppeal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserScreen(
    modifier: Modifier,
    firebaseViewModel:FirebaseViewModel,
    currentUser:FirebaseUser?,
    onClickLoginButton:() -> Unit,
    onClickLogoutButton:() -> Unit,
){
    var ProfileCurrentUser by rememberSaveable { mutableStateOf(currentUser) }
    val context = LocalContext.current
    val state = rememberPagerState (
        pageCount = {2},
        initialPage = 0
    )
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val height = systemBarsPadding.calculateTopPadding()
    val scrollPage = listOf("プロフィール","アピール")
    val scope = rememberCoroutineScope()
    val userData by firebaseViewModel.userData.collectAsState()

    LaunchedEffect(Unit) {
        val uid = firebaseViewModel.auth.currentUser?.uid
        if(uid != null){
            firebaseViewModel.startLeadingUserData(uid)
        }
    }


    if(ProfileCurrentUser == null){
            NoUserProfileHeader(
                onClickLoginButton = onClickLoginButton
            )
        }else{
            Scaffold(
                topBar = {
                    ProfileTopBar(
                        height = height,
                        ProfileCurrentUser = currentUser,
                        context = context,
                        state = state,
                        scrollPage = scrollPage,
                        scope = scope
                    )
                }
            ) { innerPadding ->
                HorizontalPager(
                    state = state,
                    modifier = Modifier
                        .background(Color.White)
                ) {
                    when(it){
                        0 ->
                            UserProfileScreen(
                                modifier = modifier
                                    .padding(innerPadding),
                                onClickLogoutButton = onClickLogoutButton,
                                SwitchProfileCurrentUser = {
                                    ProfileCurrentUser = null
                                },
                                userData = userData
                            )

                        1 ->
                            UserProfileAppeal(
                                modifier = modifier
                                    .padding(innerPadding),
                                userData = userData,
                                updateOnMyProfile = firebaseViewModel::updateOnMyProfile,
                                firebaseViewModel = firebaseViewModel
                            )
                    }
                }
            }
        }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileTopBar(
    height:Dp,
    ProfileCurrentUser:FirebaseUser?,
    context: Context,
    state:PagerState,
    scrollPage:List<String>,
    scope:CoroutineScope
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = height)
            .height(230.dp)
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        ProfileCurrentUser?.photoUrl?.let{
            Spacer(modifier = Modifier.height(12.dp))
            AsyncImage(
                model = ImageRequest.Builder(context = context)
                    .data(it)
                    .crossfade(true)
                    .build(),
                contentDescription = "ProfileImage",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(100.dp)
                    .clip(RoundedCornerShape(80.dp))
            )
        }

        ProfileCurrentUser?.displayName?.let{
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                fontWeight = FontWeight(1000),
                fontSize = 20.sp
            )
        }

        ProfileCurrentUser?.uid?.let{
            Spacer(modifier = Modifier.height((8.dp)))
            Text(
                text = "ID:${it}",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        TabRow(
            selectedTabIndex = state.currentPage,
            modifier = Modifier
                .height(40.dp)
                .align(Alignment.CenterHorizontally)
                .background(Color.Black)
                .fillMaxWidth(),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[state.currentPage]),
                    height = 3.dp,
                    color = Color(0xFF00B900)  // ここで指定した色を使用
                )
            },
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
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color =
                        if(index == state.currentPage)Color(0xFF00B900)
                        else Color.Gray
                    )
                }
            }
        }
    }
}

