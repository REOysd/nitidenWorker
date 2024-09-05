
package jp.ac.jec.cm01xx.nitidenworker.Navigation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import jp.ac.jec.cm01xx.nitidenworker.CredentialManagerAuthentication
import jp.ac.jec.cm01xx.nitidenworker.compose.FirebaseViewModel.FirebaseViewModel
import jp.ac.jec.cm01xx.nitidenworker.compose.FavoriteScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.HomeScreen.HomeScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.JobScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.RequestServiceScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen.ServiceOfferingCreationPreview
import jp.ac.jec.cm01xx.nitidenworker.compose.MessageScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.SearchScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen.ServiceOfferingsDetailViewingScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.serviceOfferingCreateScreen.ServiceOfferingCreationScreen
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
    val serviceOffering by firebaseViewModel.serviceOfferingData.collectAsState()

    LaunchedEffect(currentBackStackEntry) {
        navigationViewModel.setBottomBarVisible(true)
    }

    Scaffold(
        topBar = {
            if(
                currentBackStackEntry != NavigationScreen.User.name &&
                currentBackStackEntry != NavigationScreen.MyJob.name &&
                currentBackStackEntry != NavigationScreen.Home.name
                ){
                TopBarContext(
                    onClickToPopBackStack = { navHostController.popBackStack() },
                    backStackEntry = currentBackStackEntry
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
                    if(serviceOfferingData != null){
                        serviceOfferingData?.let {
                            NavigateFloatingActionButtonOnPreview(
                                publishServiceOfferings = firebaseViewModel::publishServiceOfferings,
                                setServiceOfferingData = navigationViewModel::setServiceOfferingData,
                                data = serviceOfferingData,
                                userData = userData,
                                onClickToMyJob = { navHostController.navigate(NavigationScreen.MyJob.name) },
                                context = LocalContext.current
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = NavigationScreen.Home.name
        ) {
            composable(NavigationScreen.Home.name){
                HomeScreen(
                    uid = firebaseViewModel.auth.currentUser?.uid,
                    getServiceOfferings = firebaseViewModel::getServiceOfferings,
                    serviceOfferings_ = firebaseViewModel.serviceOfferings,
                    getServiceOfferingData = firebaseViewModel::getServiceOfferingData,
                    onClickToServiceOfferingDetailScreen = {
                        navHostController.navigate(NavigationScreen.serviceOfferingsDetail.name)
                    },
                    cleanServiceOfferingData = firebaseViewModel::cleanServiceOfferingData,
                    cleanServiceOfferingCreationPreview = { navigationViewModel.setServiceOfferingData(null) },
                    onClickTOProfile = {navHostController.navigate(NavigationScreen.User.name)},
                    onClickHeartAndFavoriteIcon = firebaseViewModel::onClickHeartAndFavoriteIcon,
                    updateLikedUsers = firebaseViewModel::updateListTypeOfServiceOffering,
                    updateFavoriteUsers = firebaseViewModel::updateListTypeOfServiceOffering,
                    modifier = Modifier
                        .nestedScroll(navigationViewModel.nestScrollConnection),
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
                    uid = firebaseViewModel.auth.currentUser?.uid,
                    getMyFavoriteServiceOfferings = firebaseViewModel::getMyFavoriteServiceOfferings,
                    getServiceOfferingData = firebaseViewModel::getServiceOfferingData,
                    updateLikedUsers = firebaseViewModel::updateListTypeOfServiceOffering,
                    updateFavoriteUsers = firebaseViewModel::updateListTypeOfServiceOffering,
                    onClickHeartAndFavoriteIcon = firebaseViewModel::onClickHeartAndFavoriteIcon,
                    onClickToServiceOfferingDetailScreen = {
                        navHostController.navigate(NavigationScreen.serviceOfferingsDetail.name)
                    },
                    _myFavoriteServiceOfferings = firebaseViewModel.myFavoriteServiceOfferings,
                    cleanServiceOfferingCreationPreview = {
                        navigationViewModel.setServiceOfferingData(
                            null
                        )
                    },
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
                    auth_ = firebaseViewModel.auth,
                    userData = userData,
                    startLeadingUserData = firebaseViewModel::startLeadingUserData,
                    onClickToProfile = {navHostController.navigate(NavigationScreen.User.name)},
                    onClickToServiceOfferingsScreen = {
                        navHostController.navigate(NavigationScreen.serviceOfferings.name)
                    },
                    onClickToRequestServiceScreen = {
                        navHostController.navigate(NavigationScreen.requestService.name)
                    },
                    onClickToServiceOfferingsDetailScreen = { firebaseViewModel.getServiceOfferingData(it) },
                    getMyServiceOfferings = { firebaseViewModel.getMyServiceOfferings() },
                    myServiceOfferings = firebaseViewModel.myServiceOfferings,
                    cleanServiceOfferingData = firebaseViewModel::cleanServiceOfferingData,
                )

            }
            composable(NavigationScreen.User.name){
                val context = LocalContext.current
                val credentialManager = CredentialManager.create(context)
                val scope = rememberCoroutineScope()

                UserScreen(
                    onClickLoginButton = {
                        CredentialManagerAuthentication(
                            firebaseAuth = firebaseViewModel.auth,
                            navHostController = navHostController,
                            uploadUserPhoto = firebaseViewModel::uploadUserPhoto,
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
                ServiceOfferingCreationScreen(
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
                if(serviceOfferingData != null){
                    serviceOfferingData?.let{
                        ServiceOfferingCreationPreview(
                            firebaseViewModel = firebaseViewModel,
                            data = it,
                            onClickToPopBackStack = { navHostController.popBackStack() },
                            setServiceOfferingData = navigationViewModel::setServiceOfferingData,
                            modifier = Modifier
                                .nestedScroll(navigationViewModel.nestScrollConnection),
                        )
                    }
                }else{
                    ServiceOfferingsDetailViewingScreen(
                        uid = firebaseViewModel.auth.currentUser?.uid,
                        userData = userData,
                        startLeadingUserData = firebaseViewModel::startLeadingUserData,
                        serviceOfferingData = serviceOffering,
                        onClickToPopBackStack = { navHostController.popBackStack() },
                        setServiceOfferingData = navigationViewModel::setServiceOfferingData,
                        createThumbnail = { firebaseViewModel.createThumbnail(it) },
                        onClickToProfile = { navHostController.navigate(NavigationScreen.User.name) },
                        updateLikedAndFavoriteUsers = firebaseViewModel::updateListTypeOfServiceOffering,
                        onClickHeartAndFavoriteIcon = firebaseViewModel::onClickHeartAndFavoriteIcon,
                        modifier = Modifier
                            .nestedScroll(navigationViewModel.nestScrollConnection)
                    )
                }
            }
            composable(NavigationScreen.requestService.name){
                RequestServiceScreen(
                    getMyServiceOfferings = firebaseViewModel::getMyServiceOfferings,
                    myServiceOfferings = firebaseViewModel.myServiceOfferings,
                    modifier = Modifier
                        .nestedScroll(navigationViewModel.nestScrollConnection),
                    onClickToServiceOfferingsDetailScreen = firebaseViewModel::getServiceOfferingData,
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

