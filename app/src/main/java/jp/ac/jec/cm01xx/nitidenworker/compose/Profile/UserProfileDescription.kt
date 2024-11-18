package jp.ac.jec.cm01xx.nitidenworker.compose.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun UserProfileDescription(modifier: Modifier) {
    Column(
        modifier = modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        UserResultAndRoundOfApplause()
        Spacer(modifier = Modifier.height(36.dp))
        UserDepartment()
        Spacer(modifier = Modifier.height(30.dp))
        UserPR()
        Spacer(modifier = Modifier.height(30.dp))
        UserURL()
    }
}

@Composable
fun UserResultAndRoundOfApplause() {
    Row(
        modifier = Modifier
            .background(Color.Gray)
            .height(80.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .weight(1f)
                .fillMaxHeight()
                .padding(top = 14.dp)
        ) {
            Text(
                text = stringResource(id = R.string.UserProfileScreen_achievements),
                color = Color.Gray,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.hiragino_regular)),
                    fontSize = 13.sp
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "--件",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.hiragino_regular)),
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            )
        }

        HorizontalDivider(
            color = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier
                .width(0.2.dp)
        )
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxHeight()
                .weight(1f)
                .padding(top = 14.dp)
        ) {
            Text(
                text = stringResource(id = R.string.UserProfileScreen_completionRate),
                color = Color.Gray,
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.hiragino_regular)),
                    fontSize = 13.sp
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "--件",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.hiragino_regular)),
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
fun UserDepartment() {
    Column {
        Text(
            text = "基本情報",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.hiragino_bold))
            ),
            modifier = Modifier
                .padding(bottom = 6.dp, start = 10.dp)
        )
        Card (
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFEDED)),
            onClick = {}
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFFEFEDED))
                    .height(40.dp)
            ){
                Spacer(modifier = Modifier.width(18.dp))
                Image(
                    painter = painterResource(id = R.drawable.icons8__60___),
                    contentDescription = stringResource(id = R.string.UserProfileScreen_department_description),
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(18.dp))
                Column {
                    Text(
                        text = stringResource(id = R.string.UserProfileScreen_department),
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontFamily = FontFamily(Font(R.font.hiragino_bold))
                        ),
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = stringResource(id = R.string.UserProfileScreen_department_example),
                        fontFamily = FontFamily(Font(R.font.hiragino_bold)),
                        color = Color.Gray,
                        fontSize = 9.sp,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = stringResource(id = R.string.UserProfileScreen_department_IconButton_description),
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
            }
        }
    }
}

@Composable
fun UserPR() {
    Column {
        Text(
            text = "自己PR",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.hiragino_bold))
            ),
            modifier = Modifier
                .padding(bottom = 6.dp, start = 10.dp)
        )

        Card (
            shape = RectangleShape,
            onClick = {},
            modifier = Modifier
                .background(Color(0xFFEFEDED))
                .fillMaxWidth()
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFFEFEDED))
                    .padding(start = 18.dp, end = 14.dp)
                    .fillMaxWidth()
            ){
                Text(
                    text = stringResource(id = R.string.UserProfileAppealExample),
                    style = TextStyle(
                        fontSize = 9.sp,
                        fontFamily = FontFamily(Font(R.font.hiragino_bold)),
                        color = Color.Gray,
                        lineHeight = 16.sp
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .weight(1f)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = stringResource(id = R.string.UserProfileScreen_department_IconButton_description),
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}

@Composable
fun UserURL() {
    Column {
        Text(
            text = "URL",
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.hiragino_bold))
            ),
            modifier = Modifier
                .padding(bottom = 6.dp, start = 10.dp)
        )

        Card (
            shape = RectangleShape,
            onClick = {},
            modifier = Modifier
                .background(Color(0xFFEFEDED))
                .fillMaxWidth()
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFFEFEDED))
                    .padding(start = 18.dp, end = 14.dp)
                    .fillMaxWidth()
            ){
                Text(
                    text = stringResource(id = R.string.UserProfileAppealUrlExample),
                    style = TextStyle(
                        fontSize = 9.sp,
                        fontFamily = FontFamily(Font(R.font.hiragino_bold)),
                        color = Color.Gray,
                        lineHeight = 16.sp
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .weight(1f)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = stringResource(id = R.string.UserProfileScreen_department_IconButton_description),
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileDescriptionPreview() {
    UserProfileDescription(modifier = Modifier)
}