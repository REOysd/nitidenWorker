@file:OptIn(ExperimentalMaterial3Api::class)

package jp.ac.jec.cm01xx.nitidenworker.Navigation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import jp.ac.jec.cm01xx.nitidenworker.CredentialManagerAuthentication
import jp.ac.jec.cm01xx.nitidenworker.FirebaseViewModel
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.compose.FavoriteScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.HomeScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.JobScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.requestServiceScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.MessageScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.SearchScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.UserScreen.UserScreen
import kotlinx.coroutines.launch

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
    val nestScrollConnection = remember {
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
            if(
                backStack?.destination?.route != NavigationScreen.User.name &&
                backStack?.destination?.route != NavigationScreen.MyJob.name &&
                backStack?.destination?.route != NavigationScreen.serviceOfferings.name
                ){
                TopBarContext(
                    backStack = NavigationScreen
                        .valueOf(backStack?.destination?.route?: NavigationScreen.Home.name),
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
                BottomNavigationBarContext(
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
                        .nestedScroll(nestScrollConnection),
                    firebaseViewModel = firebaseViewModel,
                    onClickToProfile = {navHostController.navigate(NavigationScreen.User.name)},
                    onClickToServiceOfferingsScreen = {
                        navHostController.navigate(NavigationScreen.serviceOfferings.name)
                    },
                    onClickToRequestServiceScreen = {
                        navHostController.navigate(NavigationScreen.requestService.name)
                    }
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
            composable(NavigationScreen.serviceOfferings.name){
                ServiceOfferingsScreen(
                    modifier = Modifier
                        .nestedScroll(nestScrollConnection),
                    onClickToPopBackStack = {navHostController.popBackStack()}
                )
            }
            composable(NavigationScreen.requestService.name){
                requestServiceScreen(
                    modifier = Modifier
                        .nestedScroll(nestScrollConnection)
                )
            }
        }
    }
}


enum class NavigationScreen{
    Home,
    Search,
    Favorite,
    Message,
    MyJob,
    User,
    serviceOfferings,
    requestService,
}

data class BottomNavigationItems(
    val title:String,
    val selectedImageVector:Painter,
    val unSelectedImageVector:Painter,
)

