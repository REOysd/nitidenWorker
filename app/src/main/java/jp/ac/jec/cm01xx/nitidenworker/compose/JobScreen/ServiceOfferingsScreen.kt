package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ServiceOfferingsScreen(
    modifier: Modifier,
    onClickToPopBackStack:() -> Unit,
){
    var expanded by remember{ mutableStateOf(false) }
    val categories = listOf("デザイン","プログラミング","企画","ゲームプログラミング")
    var categoryText by remember { mutableStateOf("") }
    var titleText by remember { mutableStateOf("") }
    val maxTitleTextLength = 20
    var subTitleText by remember { mutableStateOf("") }
    val maxSubTitleTextLength = 40
    var descriptionText by remember { mutableStateOf("") }
    val maxDescriptionTextLength = 1000
    var deliveryDaysText by remember { mutableStateOf("") }
    val maxDeliveryDaysTextLength = 2
    var precautionsText by remember { mutableStateOf("") }
    val maxPrecautionsTextLength = 500
    val imageSelectPageCount = 5
    val imageSelectPagerState = rememberPagerState(
        pageCount = {imageSelectPageCount},
        initialPage = 0
    )
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {}
    )

    Scaffold(
        topBar = {
            ServiceOfferingsTopBar(onClickToPopBackStack = onClickToPopBackStack)
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ){
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.White)
            ) {
                Row{
                    Text(
                        text = "カテゴリ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "＊必須",
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                        .padding(start = 20.dp, end = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 20.dp)
                ) {
                    OutlinedTextField(
                        value = categoryText,
                        onValueChange = {categoryText = it},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedIndicatorColor = Color(0xFF00B900),
                            unfocusedIndicatorColor = Color.Gray,
                        ),
                        placeholder = {
                            Text(
                                text = "カテゴリを選択してください",
                                color = Color.Gray.copy(alpha = 0.5f),
                            )
                                      },
                        modifier = Modifier
                            .menuAnchor()
                            .background(Color.White),
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White)
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    expanded = false
                                    categoryText = category
                                },
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .defaultMinSize(minHeight = 50.dp)
                    .wrapContentHeight()
            ) {
                Row{
                    Text(
                        text = "タイトル",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "＊必須",
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                        .padding(start = 20.dp, end = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = titleText,
                        onValueChange = { newText ->
                            if(newText.length <= maxTitleTextLength){
                                titleText = newText
                            }
                        },
                        maxLines = 1,
                        placeholder = {
                            Text(
                                text = "タイトルを入力",
                                color = Color.Gray.copy(alpha = 0.5f)
                            )
                                      },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00B900),
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = Color(0xFF00B900)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 60.dp)
                            .padding(start = 20.dp, end = 20.dp),
                    )

                    Text(
                        text = "${titleText.length}/${maxTitleTextLength}",
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .defaultMinSize(minHeight = 70.dp)
                    .wrapContentHeight()
            ){
                Row{
                    Text(
                        text = "サブタイトル（タイトルの補足説明）",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "＊必須",
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                        .padding(start = 20.dp, end = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = subTitleText,
                        onValueChange = { newText ->
                            if(newText.length <= maxSubTitleTextLength){
                                subTitleText = newText
                            }
                        },
                        placeholder = {
                            Text(
                                text = "サブタイトルを入力",
                                color = Color.Gray.copy(alpha = 0.5f)
                            )
                                      },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00B900),
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = Color(0xFF00B900)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 60.dp)
                            .padding(start = 20.dp, end = 20.dp),
                    )

                    Text(
                        text = "${subTitleText.length}/${maxSubTitleTextLength}",
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .wrapContentHeight()
            ){
                Row{
                    Text(
                        text = "サービス内容の説明",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "＊必須",
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                        .padding(start = 20.dp, end = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = descriptionText,
                        onValueChange = { newText ->
                            if(newText.length <= maxDescriptionTextLength){
                                descriptionText = newText
                            }
                        },
                        placeholder = {
                            Text(
                                text = "提供できるサービス内容の説明",
                                color = Color.Gray.copy(alpha = 0.5f)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00B900),
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = Color(0xFF00B900)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 200.dp)
                            .padding(start = 20.dp, end = 20.dp),

                    )
                    Text(
                        text = "${descriptionText.length}/${maxDescriptionTextLength}",
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .wrapContentHeight()
            ){
                Row{
                    Text(
                        text = "予想お届け日数",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "＊必須",
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                        .padding(start = 20.dp, end = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = deliveryDaysText,
                        onValueChange = { newText ->
                            if(newText.length <= maxDeliveryDaysTextLength){
                                deliveryDaysText = newText
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = {
                            Text(
                                text = "７",
                                color = Color.Gray.copy(alpha = 0.5f)
                            )
                        },
                        maxLines = 1,
                        suffix = { Text(text = "日")},
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00B900),
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = Color(0xFF00B900)
                        ),
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .width(100.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .wrapContentHeight()
            ){
                Row{
                    Text(
                        text = "購入を検討している方への注意事項",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                        .padding(start = 20.dp, end = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = precautionsText,
                    onValueChange = { newText ->
                        if(newText.length <= maxPrecautionsTextLength){
                            precautionsText = newText
                        }
                    },
                    placeholder = {
                        Text(
                            text = "購入を検討しているクライアントへの注意事項など",
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color(0xFF00B900),
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color(0xFF00B900)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 200.dp)
                        .padding(start = 20.dp, end = 20.dp),
                    )

                Text(
                    text = "${precautionsText.length}/${maxPrecautionsTextLength}",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 20.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .wrapContentHeight()
            ){
                Row{
                    Text(
                        text = "イメージサンプル",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Text(
                        text = "（最大５つ）",
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.2.dp)
                        .padding(start = 20.dp, end = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    HorizontalPager(
                        state = imageSelectPagerState,
                        contentPadding = PaddingValues(start = 34.dp, end = 34.dp),
                        pageSpacing = 12.dp,
                        modifier = Modifier
                            .background(Color.White)
                    ) { page ->

                        Box(
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.LightGray.copy(alpha = 0.5f))
                                .border(
                                    color = Color.Gray.copy(alpha = 0.5f),
                                    width = 1.dp
                                )
                                .combinedClickable(
                                    onClick = {
                                        photoPicker.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }
                                )
                        ){
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                            ){
                                Image(
                                    painter = painterResource(id = R.drawable.kamera_icon_by_icons8),
                                    contentDescription = "add Picture",
                                    modifier = Modifier
                                        .size(60.dp)
                                        .align(Alignment.CenterHorizontally)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "画像を追加",
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(imageSelectPageCount){ iteration ->
                            val color = if(imageSelectPagerState.currentPage == iteration) Color.DarkGray
                            else Color.LightGray

                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(6.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .align(Alignment.Top)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServiceOfferingsTopBar(
    onClickToPopBackStack:() -> Unit
){
    val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
    val height = systemBarsPadding.calculateTopPadding()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = height)
            .height(60.dp)
            .background(Color.White)
            .drawWithContent {
                drawContent()
                // 下部にのみボーダーを描画
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 0.5.dp.toPx()
                )
            }
    ){
        IconButton(
            onClick = {
                onClickToPopBackStack()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 18.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "backToScreen"
            )
        }
        Text(
            text = "serviceOfferings",
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.Center),
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.W500
        )
    }
}