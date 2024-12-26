package jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm01xx.nitidenworker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProposalConfirmButton(
    scope: CoroutineScope,
    scrollState: ScrollState,
) {
    FloatingActionButton(
        onClick = {scope.launch{ scrollState.animateScrollTo(0) } },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(start = 20.dp, end = 20.dp),
        containerColor = colorResource(id = R.color.nitidenGreen)

    ) {
        Text(
            text = stringResource(
                id = R.string.ServiceOfferingCreationScreen_confirm
            ),
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.hiragino_bold))
        )
    }
}