package jp.ac.jec.cm01xx.nitidenworker.compose.UserScreen.UserProfileHeader

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetOnProfileHeader(
    bottomSheetTitle:String,
    placeholderOnTextField:String,
    onDismiss:() -> Unit,
    sheetState: SheetState,
    onClickCheckButton:(String,String) -> Unit,
){
    val isExpanded = sheetState.currentValue == SheetValue.Expanded
    val fabOffsetY by animateDpAsState(
        targetValue = if(isExpanded) -30.dp else -390.dp,
    )
    var text by remember{ mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        modifier = Modifier
            .padding(top = 10.dp)
            .fillMaxHeight(),
        containerColor = Color.White,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Text(
                    text = bottomSheetTitle,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W900,
                    modifier = Modifier
                        .padding(start = 20.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                        .padding(start = 10.dp, end = 10.dp)
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = {text = it},
                    placeholder = {
                        Text(
                            text = placeholderOnTextField,
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .padding(20.dp)
                        .padding(bottom = 20.dp)
                        .fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(y = fabOffsetY, x = -20.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        onDismiss()
                        onClickCheckButton("job",text)
                    },
                    modifier = Modifier
                        .padding(bottom = 30.dp, end = 20.dp)
                        .size(60.dp),
                    shape = FloatingActionButtonDefaults.largeShape,
                    containerColor = Color(0xFF00B900),
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}