package jp.ac.jec.cm01xx.nitidenworker


import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import com.google.firebase.auth.GoogleAuthProvider
import jp.ac.jec.cm01xx.nitidenworker.compose.FavoriteScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.HomeScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.MessageScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.SearchScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.UserScreenFile.UserScreen
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    navHostController: NavHostController
){
    val firebaseViewModel = FirebaseViewModel()
    val backStack by navHostController.currentBackStackEntryAsState()
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val height = systemBarsPadding.calculateTopPadding()
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
            BottomBarNavigationContext(
                navigationItems = navigationItems,
                navHostController = navHostController
            )
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
                )
            }
            composable(NavigationScreen.Search.name){
                SearchScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                )
            }
            composable(NavigationScreen.Favorite.name){
                FavoriteScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                )
            }
            composable(NavigationScreen.Message.name){
                MessageScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                )
            }
            composable(NavigationScreen.MyJob.name){
                JobScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                )
            }
            composable(NavigationScreen.User.name){
                val context = LocalContext.current
                val credentialManager = CredentialManager.create(context)
                val WEB_CLIENT_ID = "899480932485-vq9dkp81a41l1kargodov0ld004sndsi.apps.googleusercontent.com"
                val scope = rememberCoroutineScope()

                UserScreen(
                    currentUser = firebaseViewModel.auth.currentUser,
                    onClickLoginButton = {
                        val googleIdOption = GetGoogleIdOption.Builder()
                            .setFilterByAuthorizedAccounts(false)
                            .setServerClientId(WEB_CLIENT_ID)
                            .build()

                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()

                        scope.launch {
                            try{
                                val result = credentialManager.getCredential(
                                    request = request,
                                    context = context
                                )
                                val credential = result.credential
                                val googleIdTokenCredential = GoogleIdTokenCredential
                                    .createFrom(credential.data)
                                val googleIdToken = googleIdTokenCredential.idToken
                                val firebaseCredential = GoogleAuthProvider.getCredential(
                                    googleIdToken,
                                    null
                                )

                                firebaseViewModel.auth.signInWithCredential(firebaseCredential)
                                    .addOnCompleteListener{ task ->
                                        if(task.isSuccessful){
                                            navHostController.navigate(NavigationScreen.User.name)
                                        }
                                    }
                            }catch (e:Exception){
                                e.printStackTrace()
                                Toast.makeText(
                                    context,
                                    "${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    onClickLogoutButton = {
                        firebaseViewModel.auth.signOut()
                        scope.launch {
                            credentialManager.clearCredentialState(
                                ClearCredentialStateRequest()
                            )
                        }
                    }
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

