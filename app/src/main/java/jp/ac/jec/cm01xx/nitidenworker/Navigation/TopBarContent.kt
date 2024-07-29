package jp.ac.jec.cm01xx.nitidenworker.Navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun TopBarContext(
    backStack: NavigationScreen,
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
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 0.5.dp.toPx()
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
            IconButton(
                onClick = {
                    navHostController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 18.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "backToScreen"
                )
            }
            Text(
                text = backStack.name,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.Center),
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.W500
            )
        }
    }

}