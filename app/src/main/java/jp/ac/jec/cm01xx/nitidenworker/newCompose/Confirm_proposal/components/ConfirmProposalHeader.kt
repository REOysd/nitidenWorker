package jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R
import jp.ac.jec.cm01xx.nitidenworker.newCompose.common.EvaluationButton

@Composable
fun ConfirmProposalHeader(
    proposalTitleText: String,
    proposalSubTitleText: String,
    isLikeButtonSelected: Boolean,
    isFavoriteButtonSelected: Boolean,
    likeCount: Int,
    favoriteCount: Int,
    onLikeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .padding(top = 20.dp, bottom = 4.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.Start
    ) {
        ConfirmProposalTitleAnsSubTitleText(
            proposalTitleText = proposalTitleText,
            proposalSubTitleText = proposalSubTitleText
        )
        Spacer(modifier = Modifier.height(12.dp))
        ConfirmProposalEvaluationButtons(
            isLikeButtonSelected = isLikeButtonSelected,
            isFavoriteButtonSelected = isFavoriteButtonSelected,
            likeCount = likeCount,
            favoriteCount = favoriteCount,
            onLikeClick = onLikeClick,
            onFavoriteClick = onFavoriteClick,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun ConfirmProposalTitleAnsSubTitleText(
    proposalTitleText: String,
    proposalSubTitleText: String
) {
    Text(
        text = proposalTitleText,
        style = TextStyle(
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.hiragino_black)),
        )
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = proposalSubTitleText,
        style = TextStyle(
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.hiragino_medium)),
        )
    )
}

@Composable
fun ConfirmProposalEvaluationButtons(
    modifier: Modifier = Modifier,
    isLikeButtonSelected: Boolean,
    isFavoriteButtonSelected: Boolean,
    likeCount: Int,
    favoriteCount: Int,
    onLikeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isLikeButtonEnable: Boolean = true,
    isFavoriteButtonEnable: Boolean = true
) {
    Row(
        modifier = modifier
    ) {
        EvaluationButton(
            buttonIcon = Icons.Default.Favorite,
            contentDescription = "Like Button",
            onClick = onLikeClick,
            evaluationCount = likeCount,
            enable = isLikeButtonEnable,
            isSelected = isLikeButtonSelected,
            selectedColor = Color(0xFFD9D9D9),
            unselectedColor = Color(0xFFCC4F4F)
        )
        Spacer(modifier = Modifier.width(16.dp))
        EvaluationButton(
            buttonIcon = Icons.Default.Star,
            contentDescription = "Favorite Button",
            onClick = onFavoriteClick,
            evaluationCount = favoriteCount,
            enable = isFavoriteButtonEnable,
            iconSize = 28.dp,
            isSelected = isFavoriteButtonSelected,
            selectedColor = Color(0xFFD9D9D9),
            unselectedColor = Color(0xFFF3DE3A)
        )
    }
}