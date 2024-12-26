package jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import jp.ac.jec.cm01xx.nitidenworker.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProposalImagePicker(
    imageSelectPagerState: PagerState,
    selectImages: List<Uri?>,
    imageSelectPageCount: Int,
    onDeleteImage: (Int) -> Unit,
    onSelectImage: () -> Unit,
) {
    HorizontalPager(
        state = imageSelectPagerState,
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp),
        pageSpacing = 12.dp,
        modifier = Modifier
            .background(Color.White)
    ) { page ->
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFD9D9D9))
                .border(
                    color = Color(0xFF6F6F6F),
                    width = 0.5.dp
                )
                .combinedClickable(
                    onClick = onSelectImage
                )
        ) {

            if (selectImages[page] != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(selectImages[page]),
                        contentDescription = stringResource(
                            id = R.string.ServiceOfferingCreationScreen_selectImage_description
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                    )

                    FilledIconButton(
                        onClick = { onDeleteImage(page) },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .clip(CircleShape)
                            .padding(start = 10.dp, top = 10.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            Color.White.copy(
                                alpha = 0.9f
                            )
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.ServiceOfferingCreationScreen_deleteImageIcon_description)
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kamera_icon_by_icons8),
                        contentDescription = stringResource(id = R.string.ServiceOfferingCreationScreen_addImageIcon_description),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(
                            id = R.string.ServiceOfferingCreationScreen_addImage
                        ),
                        fontFamily = FontFamily(Font(R.font.hiragino_medium)),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(imageSelectPageCount) { iteration ->
            val color =
                if (imageSelectPagerState.currentPage == iteration) Color.DarkGray
                else Color.LightGray

            Box(
                modifier = Modifier
                    .size(20.dp)
                    .padding(6.dp)
                    .clip(CircleShape)
                    .background(color)
                    .align(Alignment.Top)
            )
        }
    }
}

@Composable
fun ProposalMoviePicker(
    selectMovie: Uri?,
    onDeleteMovie: () -> Unit,
    onSelectMovie: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(start = 24.dp, end = 24.dp)
            .background(Color(0xFFD9D9D9))
            .border(
                color = Color(0xFF6F6F6F),
                width = 0.5.dp
            )
            .clickable(onClick = onSelectMovie)
    ) {
        if (selectMovie != null) {
            ProposalMovieThumbnail(videoUri = selectMovie)
            FilledIconButton(
                onClick = { onDeleteMovie() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clip(CircleShape)
                    .padding(start = 10.dp, top = 10.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    Color.White.copy(
                        alpha = 0.9f
                    )
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete Movie"
                )
            }
        } else {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.movie_icon_by_icons8),
                    contentDescription = "Add video",
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.ServiceOfferingCreationScreen_addVideo),
                    fontFamily = FontFamily(Font(R.font.hiragino_medium)),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun ProposalMovieThumbnail(videoUri: Uri) {
    val context = LocalContext.current
    val mediaItem = remember(videoUri) { MediaItem.fromUri(videoUri) }
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(mediaItem)
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
