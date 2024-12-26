package jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun ProposalAlertText(
    alertText:String,
    isError:Boolean,
    modifier: Modifier = Modifier
){
    if(isError){
        Text(
            text = alertText,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.hiragino_medium)),
                color = MaterialTheme.colorScheme.error,
            ),
            modifier = modifier
                .padding(start = 20.dp),
        )
    }
}