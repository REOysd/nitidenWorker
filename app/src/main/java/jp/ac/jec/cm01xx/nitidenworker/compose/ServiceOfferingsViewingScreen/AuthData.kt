package jp.ac.jec.cm01xx.nitidenworker.compose.ServiceOfferingsViewingScreen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun AuthData(
    uid:String?,
    itemUid:String,
    likedUsers:List<String?>?,
    favoriteUsers:List<String?>?,
    photoUrl:String?,
    context:Context,
    name:String?,
    job:String?,
    updateLikedUsers:() -> Unit,
    updateFavoriteUsers:() -> Unit,
    onClickHeartIcon:(Boolean) -> Unit,
    onClickFavoriteIcon:(Boolean) -> Unit
){
    Spacer(modifier = Modifier.height(6.dp))

    HorizontalDivider(
        color = Color.Gray.copy(alpha = 0.5f),
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    )

    Row(
        modifier = Modifier
            .background(Color.White)
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        photoUrl?.let {
            AsyncImage(
                model = ImageRequest.Builder(context = context)
                    .data(it)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.UserPhoto_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(35.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column {

            Spacer(modifier = Modifier.height(8.dp))

            name?.let {
                Text(
                    text = it,
                    fontSize = 10.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            job?.let {
                Text(
                    text = it,
                    fontSize = 8.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        FavoriteIconAndNiceIcon(
            uid = uid,
            itemUid = itemUid,
            likedUsers = likedUsers,
            favoriteUsers = favoriteUsers,
            updateLikedUsers = updateLikedUsers,
            updateFavoriteUsers = updateFavoriteUsers,
            onClickHeartIcon = {
                onClickHeartIcon(it)
            },
            onClickFavoriteIcon = {
                onClickFavoriteIcon(it)
            },
            modifier = Modifier
                .align(Alignment.Top)
        )

        Spacer(modifier = Modifier.width(8.dp))
    }

    Spacer(modifier = Modifier.height(3.dp))
}