package jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun ProposalItemTitle(
    itemTitle:String,
    itemSubTitle:String? = null,
    isShowRequired:Boolean = false,
) {
    Column {
        Row{
            Text(
                text = itemTitle,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.hiragino_black))
                ),
                modifier = Modifier
                    .padding(start = 12.dp)
            )
            itemSubTitle?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.hiragino_medium))
                    ),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            if (isShowRequired) {
                Text(
                    text = stringResource(id = R.string.indispensable),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.hiragino_medium)),
                        color = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))
    }
}