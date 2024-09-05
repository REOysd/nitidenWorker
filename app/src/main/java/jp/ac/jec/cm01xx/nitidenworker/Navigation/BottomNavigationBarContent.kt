package jp.ac.jec.cm01xx.nitidenworker.Navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun BottomNavigationBarContext(
    navigationItems: List<NavigationItem>,
    navHostController: NavHostController,
    selectedItemIndex:Int,
    onSelectedItemIndexChange:(Int) -> Unit,
){

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 14.dp,
        modifier = Modifier
            .border(
                width = 0.3.dp,
                color = Color.Gray

            )
    ) {
        navigationItems.forEachIndexed { index, bottomNavigationItems ->
            NavigationBarItem(
                selected = index == selectedItemIndex,
                onClick = {
                    onSelectedItemIndexChange(index)
                    navHostController.navigate(bottomNavigationItems.route)
                },
                icon = {
                    if(index == selectedItemIndex){
                        Icon(
                            painter = painterResource(bottomNavigationItems.unSelectedIcon),
                            contentDescription = bottomNavigationItems.title,
                            modifier = Modifier
                                .size(20.dp),
                            tint = colorResource(id = R.color.bottomNavigationBarColor)
                        )
                    }else{
                        Icon(
                            painter = painterResource(bottomNavigationItems.selectedIcon),
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
                            color = colorResource(id = R.color.bottomNavigationBarColor)
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
