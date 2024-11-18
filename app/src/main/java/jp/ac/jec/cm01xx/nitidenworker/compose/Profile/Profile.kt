package jp.ac.jec.cm01xx.nitidenworker.compose.Profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserProfile(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val state = rememberPagerState (
        pageCount = {2},
        initialPage = 0
    )

    Scaffold (
        topBar = { ProfileHeader(state = state, scope = scope) }
    ){innerPadding ->
        HorizontalPager(
            state = state,
            modifier = Modifier
                .background(Color.White)
        ) {
            when(it){
                0 ->
                    UserProfileDescription(modifier = modifier.padding(innerPadding))

                1 ->
                    UserSetting(modifier = modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun UserProfilePreview() {
    UserProfile()
}