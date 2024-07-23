package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen

import android.net.Uri
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
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import jp.ac.jec.cm01xx.nitidenworker.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ServiceOfferingsScreen(
    modifier: Modifier,
    onClickToPopBackStack:() -> Unit,
    publishServiceOfferingsData:(ServiceOfferingData) -> Unit,
    onClickToServiceOfferingDetailScreen:() -> Unit
){
    var data: ServiceOfferingData? = null
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var expanded by rememberSaveable { mutableStateOf(false) }
    val categories = listOf("デザイン","プログラミング","企画","ゲームプログラミング")
    var categoryText by rememberSaveable { mutableStateOf(data?.category?:"") }
    var categoryTextIsError by rememberSaveable { mutableStateOf(false) }
    var titleText by rememberSaveable { mutableStateOf(data?.title?:"") }
    val maxTitleTextLength = 25
    var titleTextIsError by rememberSaveable { mutableStateOf(false) }
    var subTitleText by rememberSaveable { mutableStateOf(data?.subTitle?:"") }
    val maxSubTitleTextLength = 70
    var subTitleTextIsError by rememberSaveable { mutableStateOf(false) }
    var descriptionText by rememberSaveable { mutableStateOf(data?.description?:"") }
    val maxDescriptionTextLength = 1000
    var descriptionTextIsError by rememberSaveable { mutableStateOf(false) }
    var deliveryDaysText by rememberSaveable { mutableStateOf(data?.deliveryDays?:"") }
    val maxDeliveryDaysTextLength = 2
    var deliveryDaysTextIsError by rememberSaveable { mutableStateOf(false) }
    var precautionsText by rememberSaveable { mutableStateOf(data?.precautions?:"") }
    val maxPrecautionsTextLength = 500
    val imageSelectPageCount = 5
    val imageSelectPagerState = rememberPagerState(
        pageCount = {imageSelectPageCount},
        initialPage = 0
    )
    val currentImageSelectPage = imageSelectPagerState.currentPage
    var selectImages by rememberSaveable{ mutableStateOf(data?.selectImages?:List(imageSelectPageCount){null as Uri?}) }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let{
                selectImages = selectImages.toMutableList().also { it[currentImageSelectPage] = uri }
            }
        }
    )
    val movieSelectPageCount = 2
    val movieSelectPagerState = rememberPagerState (
        pageCount = { movieSelectPageCount },
        initialPage = 0
    )
    val currentMovieSelectPage = movieSelectPagerState.currentPage
    var selectMovies by rememberSaveable { mutableStateOf(data?.selectMovies?:List(movieSelectPageCount){null as Uri?}) }
    val moviePiker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                selectMovies = selectMovies.toMutableList().also { it[currentMovieSelectPage] = uri }
            }

        }
    )
    var checkBoxState by rememberSaveable{ mutableStateOf(data?.checkBoxState?:false) }

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
                .verticalScroll(scrollState)
        ){
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = modifier
                    .fillMaxWidth()
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
                        onValueChange = {
                            categoryText = it
                            if(categoryText != ""){
                                categoryTextIsError = false
                            }
                                        },
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
                        isError = categoryTextIsError,
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

                                    if(categoryText != ""){
                                        categoryTextIsError = false
                                    }
                                },
                            )
                        }
                    }
                }


                AlertText(
                    alertText = "カテゴリを入力してください",
                    isError = categoryTextIsError,
                    modifier = Modifier.padding(top = 16.dp)
                )

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

                            if(titleText != ""){
                                titleTextIsError = false
                            }
                        },
                        maxLines = 2,
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
                        isError = titleTextIsError,
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
                    
                    AlertText(
                        alertText = "タイトルを入力してください",
                        isError = titleTextIsError
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

                            if(subTitleText != ""){
                                subTitleTextIsError = false
                            }
                        },
                        placeholder = {
                            Text(
                                text = "サブタイトルを入力",
                                color = Color.Gray.copy(alpha = 0.5f)
                            )
                                      },
                        maxLines = 3,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF00B900),
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = Color(0xFF00B900)
                        ),
                        isError = subTitleTextIsError,
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

                AlertText(
                    alertText = "サブタイトルを入力してください",
                    isError = subTitleTextIsError
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

                            if(descriptionText != ""){
                                descriptionTextIsError = false
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
                        isError = descriptionTextIsError,
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

                AlertText(
                    alertText = "サービス内容を入力してください",
                    isError = descriptionTextIsError
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

                            if(deliveryDaysText != ""){
                                deliveryDaysTextIsError = false
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
                        isError = deliveryDaysTextIsError,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .width(100.dp),
                    )
                }

                AlertText(
                    alertText = "予想お届け日数を入力してください",
                    isError = deliveryDaysTextIsError,
                    modifier = Modifier.padding(top = 16.dp)
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
                        text = "画像サンプル",
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
                        ) {

                            if (selectImages[page] != null) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(selectImages[page]),
                                        contentDescription = "selectImage",
                                        modifier = Modifier
                                            .fillMaxSize()
                                    )

                                    FilledIconButton(
                                        onClick = {
                                            selectImages = selectImages.toMutableList()
                                                .also { it[currentImageSelectPage] = null }
                                        },
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .clip(CircleShape)
                                            .padding(start = 10.dp, top = 10.dp),
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            Color.White.copy(
                                                alpha = 0.9f
                                            )
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "delete Picture"
                                        )
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                ) {
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
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(imageSelectPageCount) { iteration ->
                            val color =
                                if (imageSelectPagerState.currentPage == iteration) Color.DarkGray
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .wrapContentHeight()
                    ) {
                        Row {
                            Text(
                                text = "動画サンプル",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W900,
                                modifier = Modifier
                                    .padding(start = 20.dp)
                            )

                            Text(
                                text = "（最大2つ）",
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
                                state = movieSelectPagerState,
                                contentPadding = PaddingValues(start = 34.dp, end = 34.dp),
                                pageSpacing = 12.dp,
                                modifier = Modifier
                                    .background(Color.White)
                            ){ page ->
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
                                                moviePiker.launch(
                                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                                )
                                            }
                                        )
                                ){
                                    if(selectMovies[page] != null){
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            VideoThumbnail(videoUri = selectMovies[page]!!)

                                            FilledIconButton(
                                                onClick = {
                                                    selectMovies = selectMovies .toMutableList()
                                                        .also { it[currentMovieSelectPage] = null }
                                                },
                                                modifier = Modifier
                                                    .align(Alignment.TopStart)
                                                    .clip(CircleShape)
                                                    .padding(start = 10.dp, top = 10.dp),
                                                colors = IconButtonDefaults.filledIconButtonColors(
                                                    Color.White.copy(
                                                        alpha = 0.9f
                                                    )
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "delete Picture"
                                                )
                                            }
                                        }
                                    }else{
                                        Column(
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.movie_icon_by_icons8),
                                                contentDescription = "add Picture",
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .align(Alignment.CenterHorizontally)
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = "動画を追加",
                                                modifier = Modifier
                                                    .align(Alignment.CenterHorizontally)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(movieSelectPageCount) { iteration ->
                                val color =
                                    if (movieSelectPagerState.currentPage == iteration) Color.DarkGray
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .wrapContentHeight()
                        ){
                            Row {
                                Text(
                                    text = "その他",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W900,
                                    modifier = Modifier
                                        .padding(start = 20.dp)
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
                                Text(
                                    text = "ビデオチャット",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W800,
                                    modifier = Modifier
                                        .padding(start = 20.dp)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(30.dp)
                                        .padding(top = 8.dp)
                                ){
                                    Checkbox(
                                        checked = checkBoxState,
                                        onCheckedChange = { checkBoxState = it },
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .padding(start = 20.dp),
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color(0xFF00B900),
                                        )
                                    )

                                    Text(
                                        text = "外部サイトでのビデオチャットを許可する",
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                                Text(
                                    text = "※Zoom, GoogleMeet,etc.",
                                    fontWeight = FontWeight.Light,
                                    color = Color.Gray,
                                    modifier = Modifier
                                        .padding(start = 65.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(30.dp))

                            Row {
                                FloatingActionButton(
                                    onClick = {
                                        if(CheckRequiredFields(
                                            categoryText = categoryText,
                                            titleText = titleText,
                                            subTitle = subTitleText,
                                            descriptionText = descriptionText,
                                            deliveryDaysText = deliveryDaysText,
                                            changeCategoryTextIsError = { categoryTextIsError = it },
                                            changeTitleTextIsError = { titleTextIsError = it },
                                            changeSubTitleTextIsError = { subTitleTextIsError = it },
                                            changeDescriptionTextIsError = { descriptionTextIsError = it },
                                            changeDeliveryDaysTextIsError = { deliveryDaysTextIsError = it }
                                        )
                                        ){
                                            scope.launch{ scrollState.animateScrollTo(0) }
                                        }else{
                                            val data = ServiceOfferingData(
                                                category = categoryText,
                                                title = titleText,
                                                subTitle = subTitleText,
                                                description = descriptionText,
                                                deliveryDays = deliveryDaysText,
                                                precautions = precautionsText,
                                                selectImages = selectImages,
                                                selectMovies = selectMovies,
                                                checkBoxState = checkBoxState
                                            )

                                            publishServiceOfferingsData(data)
                                            onClickToServiceOfferingDetailScreen()
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .padding(start = 20.dp, end = 20.dp),
                                    containerColor = Color(0xFF45c152)

                                ) {
                                    Text(
                                        text = "公開する",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                        )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(50.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoThumbnail(videoUri: Uri) {

    val context = LocalContext.current
    val mediaItem = remember(videoUri) { MediaItem.fromUri(videoUri) }
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(mediaItem)
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun AlertText(
    alertText:String,
    isError:Boolean,
    modifier: Modifier = Modifier
    ){
    if(isError){
        Row {
            Text(
                text = alertText,
                color = MaterialTheme.colorScheme.error,
                modifier = modifier
                    .padding(start = 20.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

fun CheckRequiredFields(
    categoryText:String,
    titleText:String,
    subTitle:String,
    descriptionText:String,
    deliveryDaysText:String,
    changeCategoryTextIsError:(Boolean) -> Unit,
    changeTitleTextIsError:(Boolean) -> Unit,
    changeSubTitleTextIsError:(Boolean) -> Unit,
    changeDescriptionTextIsError:(Boolean) -> Unit,
    changeDeliveryDaysTextIsError:(Boolean) -> Unit,
): Boolean {
    var returnBoolean = true

    if (categoryText == ""){
        changeCategoryTextIsError(true)
    }else{
        changeCategoryTextIsError(false)
    }

    if (titleText == ""){
        changeTitleTextIsError(true)
    }else{
        changeTitleTextIsError(false)
    }

    if(subTitle == ""){
        changeSubTitleTextIsError(true)
    }else{
        changeSubTitleTextIsError(false)
    }

    if(descriptionText == ""){
        changeDescriptionTextIsError(true)
    }else{
        changeDescriptionTextIsError(false)
    }

    if(deliveryDaysText == ""){
        changeDeliveryDaysTextIsError(true)
    }else{
        changeDeliveryDaysTextIsError(false)
    }

    if(
        categoryText != "" &&
        titleText != "" &&
        subTitle != "" &&
        descriptionText != "" &&
        deliveryDaysText != ""
        ){
        returnBoolean = false
    }


    return returnBoolean
}