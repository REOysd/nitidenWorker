package jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal.components

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents.ProposalMovieThumbnail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConfirmProposalThumbnail(
    scope: CoroutineScope,
    selectedImageAndMovie:List<String?>?,
    selectedImageAndMoviePagerState: PagerState,
) {
    fun isVideoUrl(url: String?): Boolean {
        if (url == null) return false
        // 一般的な動画ファイルの拡張子をチェック
        // 必ず拡張子をつけて保存すること
        val videoExtensions = listOf(".mp4", ".avi", ".mov", ".wmv", ".flv", ".mkv", ".webm")
        return videoExtensions.any { url.lowercase().endsWith(it) }
    }

    Box(
        modifier = Modifier
            .height(220.dp)
            .background(Color.Black)
    ){
        if (selectedImageAndMovie?.isEmpty() == true) {
            Card(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(Color.Black),
                onClick = {
                }
            ){
                Image(
                    painter = painterResource(id = R.drawable.nitiiden_icon),
                    contentDescription = stringResource(id = R.string.ViewingImage_default_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                )
            }

        }else{
            HorizontalPager(
                state = selectedImageAndMoviePagerState,
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.Black)
                    .height(200.dp)
            ){ page ->
                selectedImageAndMovie?.getOrNull(page)?.let { mediaUrl ->
                    if(isVideoUrl(mediaUrl)) {
                        Card(
                            modifier = Modifier
                                .fillMaxSize(),
                            shape = RectangleShape,
                            colors = CardDefaults.cardColors(Color.Black),
                            onClick = {
                            }
                        ) {
                            ProposalMovieThumbnail(videoUri = Uri.parse(mediaUrl))
                        }
                    }else{
                        Card(
                            modifier = Modifier
                                .fillMaxSize(),
                            shape = RectangleShape,
                            colors = CardDefaults.cardColors(Color.Black),
                            onClick = {
                            }
                        ) {
                            AsyncImage(
                                model = mediaUrl,
                                contentDescription = stringResource(
                                    id = R.string.SelectedImageAndMovie_description
                                ),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ){
                selectedImageAndMovie?.size?.let { count ->
                    repeat(count) { iteration ->
                        val color = if (selectedImageAndMoviePagerState.currentPage == iteration){
                            Color.DarkGray
                        }else{
                            Color.LightGray
                        }
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(5.dp)
                                .clip(CircleShape)
                                .background(color)
                                .align(Alignment.Top)
                                .clickable(
                                    onClick = {
                                        scope.launch {
                                            selectedImageAndMoviePagerState
                                                .animateScrollToPage(iteration)
                                        }
                                    }
                                )
                        )
                    }
                }
            }
        }
    }
}