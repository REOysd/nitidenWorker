package jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal.conponents

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun ConfirmProposalPublishButton() {
    Spacer(modifier = Modifier.height(28.dp))
    FloatingActionButton(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp),
        containerColor = colorResource(id = R.color.nitidenGreen)
    ) {
        Text(
            text = "公開する",
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.hiragino_bold))
        )
    }
    Spacer(modifier = Modifier.height(32.dp))
}