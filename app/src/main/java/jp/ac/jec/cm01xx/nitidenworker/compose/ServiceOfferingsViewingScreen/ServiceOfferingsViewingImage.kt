package jp.ac.jec.cm01xx.nitidenworker.compose.ServiceOfferingsViewingScreen

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.ac.jec.cm01xx.nitidenworker.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ServiceOfferingsViewingImage(
    selectImages: List<String?>?,
    selectMovies: List<String?>?,
    selectMovieThumbnail:String?
){
    if(selectImages?.isEmpty() == true && selectMovies?.isEmpty() == true){
        DefaultImage()

    }else if(selectImages?.isNotEmpty() == true){
        selectImages[0]?.let{
            ImageThumbnail(selectImages = it)
        }

    }else if(selectMovies?.isNotEmpty() == true){
        selectMovies[0]?.let{
            MovieThumbnail( selectMovieThumbnail = selectMovieThumbnail)
        }
    }
}

@Composable
fun RecruitmentBadge(
    modifier: Modifier
){
    Card(
        colors = CardDefaults.cardColors(Color(0xFF47c6c6)),
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Text(
            text = "現在募集中!!",
            color = Color.White,
            fontSize = 8.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 15.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun DefaultImage(){
    Image(
        painter = painterResource(id = R.drawable.nitiiden_icon),
        contentDescription = null,
        modifier = Modifier
            .height(80.dp)
            .width(80.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
    )
}

@Composable
fun ImageThumbnail(selectImages:String){
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(selectImages)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(80.dp)
            .width(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
    )
}

@Composable
fun MovieThumbnail(selectMovieThumbnail:String?){

    Box(
        modifier = Modifier
            .height(80.dp)
            .width(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
    ){
        if(selectMovieThumbnail != null){

            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(selectMovieThumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = "MovieThumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                )

            IconButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(20.dp),
                colors = IconButtonDefaults.iconButtonColors(Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "play video",
                    modifier = Modifier
                        .size(20.dp),
                    tint = Color.Black
                )
            }
        }else{

            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(30.dp),
                color = Color.Gray,
                strokeWidth = 3.dp
            )
        }
    }
}