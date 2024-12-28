package jp.ac.jec.cm01xx.nitidenworker.newCompose.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EvaluationButton(
    buttonIcon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    evaluationCount: Int,
    enable: Boolean,
    fontSize: TextUnit = 18.sp,
    iconSize: Dp = 24.dp,
    isSelected: Boolean,
    selectedColor: Color,
    unselectedColor: Color,
) {
    Row (
        modifier = Modifier.background(Color.White),
    ){
        IconButton(
            onClick = { onClick() },
            enabled = enable,
        ) {
            Icon(
                imageVector = buttonIcon,
                contentDescription = contentDescription,
                tint = if (isSelected) unselectedColor else selectedColor,
                modifier = Modifier.size(iconSize)
            )
        }
        Text(
            text = evaluationCount.toString(),
            style = TextStyle(
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}