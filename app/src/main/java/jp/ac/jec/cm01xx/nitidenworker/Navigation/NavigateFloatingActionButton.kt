package jp.ac.jec.cm01xx.nitidenworker.Navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.FirebaseViewModel
import jp.ac.jec.cm01xx.nitidenworker.UserDocument
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen.ServiceOfferingData

@Composable
fun NavigateFloatingActionButtonOnBottom(
    publishServiceOfferings:(ServiceOfferingData) -> Unit,
    data:ServiceOfferingData?,
    userData: UserDocument?,
    onClickToMyJob:() -> Unit,
){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 16.dp)
    ) {
        FloatingActionButton(
            onClick = {
                if(data != null && userData != null){
                    publishServiceOfferings(data)
                }
                onClickToMyJob()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(53.dp)
                .padding(horizontal = 80.dp),
            shape = RoundedCornerShape(34.dp),
            containerColor = Color(0xFF45c152),
        ) {
            Text(
                text = "公開する",
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 15.sp
            )
        }
    }
}