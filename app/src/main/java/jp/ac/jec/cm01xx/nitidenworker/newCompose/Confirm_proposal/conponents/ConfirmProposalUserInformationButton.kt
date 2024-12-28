package jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal.conponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm01xx.nitidenworker.newCompose.common.UserInformationButton

@Composable
fun ConfirmProposalUserInformationButton() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD9D9D9))
                .height(8.dp)
        )
        UserInformationButton(
            userName = "ユーザーネーム",
            studentID = "24cm0137@jec.ac.jp",
            height = 70.dp,
            userIconSize = 48.dp
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD9D9D9))
                .height(8.dp)
        )
    }
}