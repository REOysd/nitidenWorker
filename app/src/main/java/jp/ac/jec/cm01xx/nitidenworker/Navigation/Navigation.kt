
package jp.ac.jec.cm01xx.nitidenworker.Navigation


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
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
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen.ServiceOfferingsDetailScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen.ServiceOfferingsScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.requestServiceScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.MessageScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.SearchScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.UserScreen.UserScreen
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    navHostController: NavHostController,
    navigationViewModel:NavigationViewModel = viewModel(),
    firebaseViewModel: FirebaseViewModel = viewModel()
){
    val serviceOfferingData by navigationViewModel.serviceOfferingData.collectAsState()
    val backStack by navHostController.currentBackStackEntryAsState()
    val currentBackStackEntry = backStack?.destination?.route
    val isBottomBarVisible by navigationViewModel.isBottomBarVisible.collectAsState()
    val selectedItemIndex by navigationViewModel.selectedItemIndex.collectAsState()
    val userData by firebaseViewModel.userData.collectAsState()

    LaunchedEffect(currentBackStackEntry) {
        navigationViewModel.setBottomBarVisible(true)
    }

    Scaffold(
        topBar = {
            if(
                currentBackStackEntry != NavigationScreen.User.name &&
                currentBackStackEntry != NavigationScreen.MyJob.name &&
                currentBackStackEntry != NavigationScreen.serviceOfferings.name
                ){
                TopBarContext(
                    backStack = NavigationScreen
                        .valueOf(backStack?.destination?.route?: NavigationScreen.Home.name),
                    navHostController = navHostController,
                    modifier = Modifier
                        .statusBarsPadding()
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
                if(backStack?.destination?.route != NavigationScreen.serviceOfferingsDetail.name){
                    BottomNavigationBarContext(
                        selectedItemIndex = selectedItemIndex,
                        onSelectedItemIndexChange = { navigationViewModel.setSelectedItemIndex(it) },
                        navigationItems = navigationViewModel.navigationItems,
                        navHostController = navHostController
                    )
                }else{
                    NavigateFloatingActionButtonOnBottom(
                        firebaseViewModel = firebaseViewModel,
                        data = serviceOfferingData,
                        userData = userData
                    )
                }
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
                        .nestedScroll(navigationViewModel.nestScrollConnection)
                )
            }
            composable(NavigationScreen.Search.name){
                SearchScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(navigationViewModel.nestScrollConnection)
                )
            }
            composable(NavigationScreen.Favorite.name){
                FavoriteScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(navigationViewModel.nestScrollConnection)
                )
            }
            composable(NavigationScreen.Message.name){
                MessageScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(navigationViewModel.nestScrollConnection)
                )
            }
            composable(NavigationScreen.MyJob.name){
                JobScreen(
                    modifier = Modifier
                        .nestedScroll(navigationViewModel.nestScrollConnection),
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
                    modifier = Modifier.nestedScroll(navigationViewModel.nestScrollConnection)
                )
            }
            composable(NavigationScreen.serviceOfferings.name){
                ServiceOfferingsScreen(
                    modifier = Modifier
                        .nestedScroll(navigationViewModel.nestScrollConnection),
                    onClickToPopBackStack = {navHostController.popBackStack()},
                    publishServiceOfferingsData = { navigationViewModel.setServiceOfferingData(it) },
                    onClickToServiceOfferingDetailScreen = {
                        navHostController.navigate(NavigationScreen.serviceOfferingsDetail.name)
                    }
                )
            }
            composable(NavigationScreen.serviceOfferingsDetail.name){
                serviceOfferingData?.let{
                    ServiceOfferingsDetailScreen(
                        firebaseViewModel = firebaseViewModel,
                        data = it,
                        onClickToPopBackStack = { navHostController.popBackStack() },
                        modifier = Modifier
                            .nestedScroll(navigationViewModel.nestScrollConnection)
                    )
                }
            }
            composable(NavigationScreen.requestService.name){
                requestServiceScreen(
                    modifier = Modifier
                        .nestedScroll(navigationViewModel.nestScrollConnection)
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
    serviceOfferingsDetail,
}

