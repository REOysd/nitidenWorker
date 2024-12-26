package jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun ProposalItemTextField(
    itemText: String,
    placeholder: String,
    itemErrorText: String? = null,
    maxTextLength: Int,
    maxLines:Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    onChangeItemText: (String) -> Unit,
    onChangeItemError: (Boolean) -> Unit = {},
    onChangeItemFocus: (Boolean) -> Unit,
    suffix: String? = null,
    isShowItemError: Boolean = false,
    isShowItemWordCount:Boolean = true,
    isItemFocused:Boolean,
    focusManager: FocusManager = LocalFocusManager.current,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .border(
                width = if (isItemFocused) 2.dp else 1.1.dp,
                color = when {
                    isShowItemError -> MaterialTheme.colorScheme.error
                    isItemFocused -> colorResource(id = R.color.bottomNavigationBarColor)
                    else -> Color.Gray
                },
                shape = RoundedCornerShape(4.dp)
            )
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BasicTextField(
                value = itemText,
                onValueChange = { newText ->
                    if (newText.length <= maxTextLength) {
                        onChangeItemText(newText)
                    }
                    if (newText.isNotEmpty()) {
                        onChangeItemError(false)
                    }
                },
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.hiragino_medium)),
                ),
                maxLines = maxLines,
                keyboardOptions = KeyboardOptions(
                    imeAction = if (maxLines == 1) ImeAction.Done else ImeAction.Default,
                    keyboardType = keyboardType
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                cursorBrush = SolidColor(
                    when {
                        isShowItemError -> MaterialTheme.colorScheme.error
                        isItemFocused -> colorResource(id = R.color.bottomNavigationBarColor)
                        else -> Color.Gray
                    }
                ),
                decorationBox = { innerTextField ->
                    if (itemText.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.hiragino_medium)),
                            color = Color.Gray.copy(alpha = 0.5f),
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        onChangeItemFocus(focusState.isFocused)
                    }
            )

            suffix?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.hiragino_medium)),
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        itemErrorText?.let {
            ProposalAlertText(
                alertText = it,
                isError = isShowItemError,
                modifier = Modifier
                    .absoluteOffset(x = -20.dp)
                    .padding(top = 10.dp)
            )
        }
        if (isShowItemWordCount) {
            Text(
                text = "${itemText.length}/${maxTextLength}",
                fontSize = 11.sp,
                fontFamily = FontFamily(Font(R.font.hiragino_medium)),
                modifier = Modifier
                    .align(Alignment.TopEnd)
            )
        }
    }
}