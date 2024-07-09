package jp.ac.jec.cm01xx.nitidenworker


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import jp.ac.jec.cm01xx.nitidenworker.compose.FavoriteScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.HomeScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.MessageScreen
import jp.ac.jec.cm01xx.nitidenworker.compose.SearchScreen

@Composable
fun Navigation(
    navHostController: NavHostController
){
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
            TopBarContext(
                backStack = NavigationScreen
                    .valueOf(backStack?.destination?.route?:NavigationScreen.Home.name),
                navHostController = navHostController,
                modifier = Modifier
                    .padding(top = height)
                    .fillMaxWidth()
            )
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
        }
    }
}

@Composable
fun BottomBarNavigationContext(
    navigationItems: List<BottomNavigationItems>,
    navHostController: NavHostController,
){
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    NavigationBar {
        navigationItems.forEachIndexed { index, bottomNavigationItems ->
            NavigationBarItem(
                selected = index == selectedItemIndex,
                onClick = {
                    selectedItemIndex = index
                    navHostController.navigate(bottomNavigationItems.title)
                },
                icon = {
                    Icon(
                        painter = if(index == selectedItemIndex){
                            bottomNavigationItems.unSelectedImageVector
                        }else{
                            bottomNavigationItems.selectedImageVector
                        },
                        contentDescription = bottomNavigationItems.title,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = { Text(text = bottomNavigationItems.title) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarContext(
    backStack:NavigationScreen,
    navHostController: NavHostController,
    modifier: Modifier
    ){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.)
    ){
        Text(text = backStack.name)
    }

}

enum class NavigationScreen{
    Home,
    Search,
    Favorite,
    Message,
    MyJob
}

data class BottomNavigationItems(
    val title:String,
    val selectedImageVector:Painter,
    val unSelectedImageVector:Painter,
)

