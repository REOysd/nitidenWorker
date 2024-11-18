package jp.ac.jec.cm01xx.nitidenworker.compose.ServiceOfferingsViewingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen.FavoriteIcon
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsDetailScreen.HeartIcon

@Composable
fun ServiceOfferingsViewingBottomItems(
    category: String,
    applyCount: String,
    deliveryDays:String
){
    Spacer(modifier = Modifier.height(12.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        ServiceOfferingsViewingSubItems(
            category = category,
            applyCount = applyCount,
            deliveryDays = deliveryDays
        )
    }
}

@Composable
fun ServiceOfferingsViewingSubItems(
    category:String,
    applyCount: String,
    deliveryDays:String
){
    Column(
        modifier = Modifier
            .height(70.dp)
    ){
        CategoryItem(category = category)

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterHorizontally)
        ) {
            ApplyingItem(
                applyCount = applyCount,
                modifier = Modifier
                    .align(Alignment.Bottom)
            )

            DeliveryDaysItem(
                deliveryDays = deliveryDays,
                modifier = Modifier
                    .align(Alignment.Bottom)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun FavoriteIconAndNiceIcon(
    uid:String?,
    itemUid:String,
    favoriteUsers:List<String?>?,
    likedUsers:List<String?>?,
    updateLikedUsers:() -> Unit,
    updateFavoriteUsers: () -> Unit,
    onClickHeartIcon:(Boolean) -> Unit,
    onClickFavoriteIcon:(Boolean) -> Unit,
    modifier: Modifier
){
    if(uid != itemUid){
        Row(
            modifier = modifier
                .background(Color.White)
        ) {
            HeartIcon(
                uid = uid,
                serviceUid = itemUid,
                likedUsers = likedUsers,
                updateLikedUsers = updateLikedUsers,
                onChangeNiceCount = {
                    onClickHeartIcon(it)
                },
                modifier = modifier.size(40.dp)
            )

            FavoriteIcon(
                uid = uid,
                serviceUid = itemUid,
                favoriteUsers = favoriteUsers,
                updateFavoriteUsers = updateFavoriteUsers,
                onChangeFavoriteCount = {
                    onClickFavoriteIcon(it)
                },
                modifier = modifier.size(40.dp)
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: String
){
    Row (
        modifier = Modifier
            .height(45.dp)
    ){
        Text(
            text = "${stringResource(id = R.string.category)} ：",
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 3.dp)
                .align(Alignment.Bottom)
        )

        Text(
            text = category,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = colorResource(id = R.color.nitidenGreen),
            modifier = Modifier
                .padding(top = 9.dp)
                .border(
                    color = colorResource(id = R.color.nitidenGreen),
                    width = 1.dp,
                    shape = RoundedCornerShape(5.dp),
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .align(Alignment.Bottom)
        )
    }
}

@Composable
fun DeliveryDaysItem(
    deliveryDays: String,
    modifier: Modifier
){
    Row (
        modifier = modifier
    ){
        Text(
            text = "${deliveryDays}：",
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 8.dp)
        )

        Text(
            text = deliveryDays,
            fontSize = 12.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 3.dp)
        )

        Text(
            text = stringResource(id = R.string.deliveryDays_day),
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 4.dp)
        )
    }
}

@Composable
fun ApplyingItem(
    modifier: Modifier,
    applyCount: String
){
    Row(
        modifier = modifier
    ){
        Text(
            text = "${stringResource(id = R.string.NumberOfApplications)}：",
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        )

        Text(
            text = applyCount,
            fontSize = 12.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(start = 3.dp)
                .align(Alignment.CenterVertically)
        )

        Text(
            text = stringResource(id = R.string.NumberOfPeople),
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier
                .padding(start = 4.dp)
                .align(Alignment.CenterVertically)
        )
    }
}