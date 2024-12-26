package jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProposalItemDropdownMenu(
    itemText: String,
    placeholder: String,
    categories: List<String>,
    expanded: Boolean,
    isShowItemError: Boolean,
    onChangeItemText: (String) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onChangeItemError: (Boolean) -> Unit,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(it) },
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        OutlinedTextField(
            value = itemText,
            onValueChange = { newText ->
                onChangeItemText(newText)
                if(newText.isNotEmpty()) {
                    onChangeItemError(false)
                }
            },
            readOnly = true,
            shape = RoundedCornerShape(4.dp),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = colorResource(id = R.color.bottomNavigationBarColor),
                unfocusedIndicatorColor = Color.Gray,
                errorIndicatorColor = MaterialTheme.colorScheme.error,
                cursorColor = colorResource(id = R.color.bottomNavigationBarColor),
                errorCursorColor = MaterialTheme.colorScheme.error,
                errorContainerColor = Color.White
            ),
            placeholder = {
                Text(
                    text = placeholder,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.hiragino_medium)),
                        color = Color.Gray.copy(alpha = 0.5f),
                    ),
                )
            },
            isError = isShowItemError,
            modifier = Modifier
                .menuAnchor()
                .background(Color.White)
                .height(50.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier.background(Color.White)
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = category,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.hiragino_medium))
                            )
                        )
                    },
                    onClick = {
                        onExpandedChange(false)
                        onChangeItemText(category)
                        if(category.isNotEmpty()) {
                            onChangeItemError(false)
                        }
                    }
                )
            }
        }
    }

    ProposalAlertText(
        alertText = stringResource(id = R.string.Category_textField_AlertText),
        isError = isShowItemError,
        modifier = Modifier.padding(top = 8.dp)
    )
}