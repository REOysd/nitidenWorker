@file:OptIn(ExperimentalMaterial3Api::class)

package jp.ac.jec.cm01xx.nitidenworker


import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Timestamp
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import jp.ac.jec.cm01xx.nitidenworker.compose.FavoriteScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.HomeScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.MessageScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.SearchScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.UserScreenFile.UserScreen
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun Navigation(
    navHostController: NavHostController
){
    val firebaseViewModel = FirebaseViewModel()
    val backStack by navHostController.currentBackStackEntryAsState()
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val height = systemBarsPadding.calculateTopPadding()
    var isBottomBarVisible by remember{ mutableStateOf(true) }
    var lastScrollOffset by remember { mutableStateOf(0f) }
    var nestScrollConnection = remember {
        object:NestedScrollConnection{
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if(delta < 0){
                    isBottomBarVisible = false
                }else if(delta > 0){
                    isBottomBarVisible = true
                }
                lastScrollOffset += delta
                return Offset.Zero
            }
        }
    }
    val navigationItems = listOf(
        BottomNavigationItems(
            title = NavigationScreen.Home.name,
            selectedImageVector = painterResource(id = R.drawable.home_icon_by_icons8),
            unSelectedImageVector = painterResource(id = R.drawable.home_icon_black_by_icons8)
        ),
        BottomNavigationItems(
            title = NavigationScreen.Search.name,
            selectedImageVector = painterResource(id = R.drawable.search_icon_by_icons8),
            unSelectedImageVector = painterResource(id = R.drawable.search_icon_black_by_icons8)
        ),
        BottomNavigationItems(
            title = NavigationScreen.Favorite.name,
            selectedImageVector = painterResource(id = R.drawable.favorite_icon_by_icons8),
            unSelectedImageVector = painterResource(id = R.drawable.favorite_icon_black_by_icons8)
        ),
        BottomNavigationItems(
            title = NavigationScreen.Message.name,
            selectedImageVector = painterResource(id = R.drawable.message_icon_by_icons8),
            unSelectedImageVector = painterResource(id = R.drawable.message_icon_black_by_icons8)
        ),
        BottomNavigationItems(
            title = NavigationScreen.MyJob.name,
            selectedImageVector = painterResource(id = R.drawable.job_icon_by_icons8),
            unSelectedImageVector = painterResource(id = R.drawable.job_icon_black_by_icons8)
        )
    )

    Scaffold(
        topBar = {
            if(backStack?.destination?.route != NavigationScreen.User.name){
                TopBarContext(
                    backStack = NavigationScreen
                        .valueOf(backStack?.destination?.route?:NavigationScreen.Home.name),
                    navHostController = navHostController,
                    modifier = Modifier
                        .padding(top = height)
                        .fillMaxWidth()
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically (initialOffsetY = {it}),
                exit = slideOutVertically (targetOffsetY = {it})
            ) {
                BottomBarNavigationContext(
                    navigationItems = navigationItems,
                    navHostController = navHostController
                )
            }
        }
    ) {innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = NavigationScreen.Home.name
        ) {
            composable(NavigationScreen.Home.name){
                HomeScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(nestScrollConnection)
                )
            }
            composable(NavigationScreen.Search.name){
                SearchScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(nestScrollConnection)
                )
            }
            composable(NavigationScreen.Favorite.name){
                FavoriteScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(nestScrollConnection)
                )
            }
            composable(NavigationScreen.Message.name){
                MessageScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(nestScrollConnection)
                )
            }
            composable(NavigationScreen.MyJob.name){
                JobScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(nestScrollConnection)
                )
            }
            composable(NavigationScreen.User.name){
                val context = LocalContext.current
                val credentialManager = CredentialManager.create(context)
                val scope = rememberCoroutineScope()

                UserScreen(
                    onClickLoginButton = {
                        CredentialManagerAuthentication(
                            firebaseViewModel = firebaseViewModel,
                            navHostController = navHostController,
                            context = context,
                            credentialManager = credentialManager,
                            scope = scope
                        )
                    },
                    onClickLogoutButton = {
                        firebaseViewModel.auth.signOut()
                        scope.launch {
                            credentialManager.clearCredentialState(
                                ClearCredentialStateRequest()
                            )
                        }
                    },
                    firebaseViewModel = firebaseViewModel,
                    currentUser = firebaseViewModel.auth.currentUser,
                    modifier = Modifier.nestedScroll(nestScrollConnection)
                )
            }
        }
    }
}

@Composable
fun BottomBarNavigationContext(
    navigationItems: List<BottomNavigationItems>,
    navHostController: NavHostController,
){
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 14.dp,
        modifier = Modifier
            .border(
                width = 0.2.dp,
                color = Color.Gray
            )
    ) {
        navigationItems.forEachIndexed { index, bottomNavigationItems ->

            NavigationBarItem(
                selected = index == selectedItemIndex,
                onClick = {
                    selectedItemIndex = index
                    navHostController.navigate(bottomNavigationItems.title)
                },
                icon = {
                    if(index == selectedItemIndex){
                        Icon(
                            painter = bottomNavigationItems.unSelectedImageVector,
                            contentDescription = bottomNavigationItems.title,
                            modifier = Modifier
                                .size(20.dp),
                            tint = Color(0xFF00B900)
                        )
                    }else{
                        Icon(
                            painter = bottomNavigationItems.selectedImageVector,
                            contentDescription = bottomNavigationItems.title,
                            modifier = Modifier
                                .size(20.dp),
                            tint = Color.Gray
                        )
                    }
                },
                label = {
                    if(index == selectedItemIndex){
                        Text(
                            text = bottomNavigationItems.title,
                            color = Color(0xFF00C000)
                        )
                    }else{
                        Text(text = bottomNavigationItems.title)
                    }
                        },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White
                ),
                modifier = Modifier
            )
        }
    }
}

@Composable
fun TopBarContext(
    backStack:NavigationScreen,
    navHostController: NavHostController,
    modifier: Modifier
    ){
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color.White)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White)
            .drawWithContent {
                drawContent()
                // 下部にのみボーダーを描画
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 0.3.dp.toPx()
                )
            }
    ){
        if(backStack == NavigationScreen.Home){
            Image(
                painter = painterResource(id = R.drawable.nitiiden_icon),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(200.dp)
                    .padding(start = 16.dp)

            )
            IconButton(
                onClick = { navHostController.navigate(NavigationScreen.User.name) },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.user_icon_by_icons8),
                    contentDescription = "Person",
                    modifier = Modifier
                        .size(28.dp),
                    tint = Color.Gray
                )
            }
            IconButton(
                onClick = {  },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 60.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                    modifier = Modifier
                        .size(28.dp),
                    tint = Color.Gray
                )
            }
        }else{
            Log.d("Navigation",backStack.name)
            Text(
                text = backStack.name,
                modifier = Modifier
                    .align(Alignment.TopCenter),
                fontSize = 40.sp,
                color = Color.Black,
                fontWeight = FontWeight(200)
            )
        }
    }

}

enum class NavigationScreen{
    Home,
    Search,
    Favorite,
    Message,
    MyJob,
    User
}

data class BottomNavigationItems(
    val title:String,
    val selectedImageVector:Painter,
    val unSelectedImageVector:Painter,
)

