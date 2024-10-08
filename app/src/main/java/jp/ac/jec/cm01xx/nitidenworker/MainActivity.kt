package jp.ac.jec.cm01xx.nitidenworker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import jp.ac.jec.cm01xx.nitidenworker.Navigation.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            Navigation(navHostController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview(){
    val navController = rememberNavController()
    Navigation(navHostController = navController)
}
