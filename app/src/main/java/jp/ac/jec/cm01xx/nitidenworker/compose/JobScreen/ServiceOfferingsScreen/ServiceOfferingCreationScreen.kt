package jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.serviceOfferingCreateScreen

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import jp.ac.jec.cm01xx.nitidenworker.ServiceOfferingData
import jp.ac.jec.cm01xx.nitidenworker.compose.JobScreen.ServiceOfferingsScreen.ServiceOfferingCreationTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ServiceOfferingCreationScreen(
    modifier: Modifier,
    onClickToPopBackStack:() -> Unit,
    publishServiceOfferingsData:(ServiceOfferingData) -> Unit,
    onClickToServiceOfferingDetailScreen:() -> Unit
){
    val data: ServiceOfferingData? = null
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var expanded by rememberSaveable { mutableStateOf(false) }
    val categories = listOf("デザイン","プログラミング","企画","ゲームプログラミング")
    var categoryText by rememberSaveable { mutableStateOf(data?.category?:"") }
    var categoryTextIsError by rememberSaveable { mutableStateOf(false) }
    var titleText by rememberSaveable { mutableStateOf(data?.title?:"") }
    val maxTitleTextLength = 30
    var titleTextIsError by rememberSaveable { mutableStateOf(false) }
    var subTitleText by rememberSaveable { mutableStateOf(data?.subTitle?:"") }
    val maxSubTitleTextLength = 60
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
            ServiceOfferingCreationTopBar(onClickToPopBackStack = onClickToPopBackStack)
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
                        text = stringResource(id = R.string.category),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.indispensable),
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
                            focusedIndicatorColor = colorResource(id = R.color.bottomNavigationBarColor),
                            unfocusedIndicatorColor = Color.Gray,
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.Category_textField_placeholder),
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
                    alertText = stringResource(id = R.string.Category_textField_AlertText),
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
                        text = stringResource(id = R.string.ServiceOfferingCreationScreen_titleText),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.indispensable),
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
                                text = stringResource(id = R.string.ServiceOfferingCreationScreen_titleText_placeholder),
                                color = Color.Gray.copy(alpha = 0.5f)
                            )
                                      },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = colorResource(id = R.color.bottomNavigationBarColor),
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = colorResource(id = R.color.bottomNavigationBarColor)
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
                        alertText = stringResource(id = R.string.ServiceOfferingCreationScreen_titleText_AlertText),
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
                        text = stringResource(id = R.string.ServiceOfferingCreationScreen_subtitleText),
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
                                text = stringResource(id = R.string.ServiceOfferingCreationScreen_subtitleText_placeholder),
                                color = Color.Gray.copy(alpha = 0.5f)
                            )
                                      },
                        maxLines = 3,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = colorResource(id = R.color.bottomNavigationBarColor),
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = colorResource(id = R.color.bottomNavigationBarColor)
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
                        text = stringResource(id = R.string.ServiceOfferingCreationScreen_descriptionOfServices),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.indispensable),
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
                                text = stringResource(
                                    id = R.string.ServiceOfferingCreationScreen_descriptionOfServices_placeholder
                                ),
                                color = Color.Gray.copy(alpha = 0.5f)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = colorResource(id = R.color.bottomNavigationBarColor),
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = colorResource(id = R.color.bottomNavigationBarColor)
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
                    alertText = stringResource(
                        id = R.string.ServiceOfferingCreationScreen_descriptionOfServices_AlertText
                    ),
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
                        text = stringResource(
                            id = R.string.ServiceOfferingCreationScreen_estimated_delivery_time
                        ),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = stringResource(id = R.string.indispensable),
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
                        suffix = { Text(stringResource(id = R.string.deliveryDays_day))},
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = colorResource(id = R.color.bottomNavigationBarColor),
                            unfocusedIndicatorColor = Color.Gray,
                            cursorColor = colorResource(id = R.color.bottomNavigationBarColor)
                        ),
                        isError = deliveryDaysTextIsError,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .width(100.dp),
                    )
                }

                AlertText(
                    alertText = stringResource(
                        id = R.string.ServiceOfferingCreationScreen_estimated_delivery_time_AlertText
                    ),
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
                        text = stringResource(id = R.string.ServiceOfferingCreationScreen_precautions),
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
                            text = stringResource(
                                id = R.string.ServiceOfferingCreationScreen_precautions_placeholder
                            ),
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = colorResource(id = R.color.bottomNavigationBarColor),
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = colorResource(id = R.color.bottomNavigationBarColor)
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
                        text = stringResource(id = R.string.ServiceOfferingCreationScreen_Image_sample),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W900,
                        modifier = Modifier
                            .padding(start = 20.dp)
                    )

                    Text(
                        text = stringResource(R.string.ServiceOfferingCreationScreen_image_limit),
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
                                        contentDescription = stringResource(
                                            id = R.string.ServiceOfferingCreationScreen_selectImage_description
                                        ),
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
                                            contentDescription = stringResource(
                                                id = R.string.ServiceOfferingCreationScreen_deleteImageIcon_description
                                            )
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
                                        contentDescription = stringResource(
                                            id = R.string.ServiceOfferingCreationScreen_addImageIcon_description
                                        ),
                                        modifier = Modifier
                                            .size(60.dp)
                                            .align(Alignment.CenterHorizontally)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = stringResource(
                                            id = R.string.ServiceOfferingCreationScreen_addImage
                                        ),
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
                                text = stringResource(
                                    id = R.string.ServiceOfferingCreationScreen_video_sample
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W900,
                                modifier = Modifier
                                    .padding(start = 20.dp)
                            )

                            Text(
                                text = stringResource(
                                    id = R.string.ServiceOfferingCreationScreen_video_limit
                                ),
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
                                                    PickVisualMediaRequest(
                                                        ActivityResultContracts.PickVisualMedia.VideoOnly
                                                    )
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
                                                    contentDescription = stringResource(
                                                        id = R.string.ServiceOfferingCreationScreen_deleteVideoIcon_description
                                                    )
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
                                                contentDescription = stringResource(
                                                    id = R.string.ServiceOfferingCreationScreen_addVideoIcon_description
                                                ),
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .align(Alignment.CenterHorizontally)
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Text(
                                                text = stringResource(
                                                    id = R.string.ServiceOfferingCreationScreen_addVideo
                                                ),
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
                                    text = stringResource(
                                        id = R.string.ServiceOfferingCreationScreen_other
                                    ),
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
                                    text = stringResource(
                                        id = R.string.ServiceOfferingCreationScreen_videoChat
                                    ),
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
                                            colorResource(id = R.color.bottomNavigationBarColor),
                                        )
                                    )

                                    Text(
                                        text = stringResource(
                                            id = R.string.ServiceOfferingCreationScreen_allowVideoChat
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                    )
                                }
                                Text(
                                    text = stringResource(
                                        id = R.string.ServiceOfferingCreationScreen_videoChat_example
                                    ),
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
                                            descriptionText = descriptionText,
                                            deliveryDaysText = deliveryDaysText,
                                            changeCategoryTextIsError = { categoryTextIsError = it },
                                            changeTitleTextIsError = { titleTextIsError = it },
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
                                                checkBoxState = checkBoxState,
                                                niceCount = 0,
                                                favoriteCount = 0,
                                                applyingCount = 0
                                            )

                                            publishServiceOfferingsData(data)
                                            onClickToServiceOfferingDetailScreen()
                                        }
                                    },
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
    descriptionText:String,
    deliveryDaysText:String,
    changeCategoryTextIsError:(Boolean) -> Unit,
    changeTitleTextIsError:(Boolean) -> Unit,
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
        descriptionText != "" &&
        deliveryDaysText != ""
        ){
        returnBoolean = false
    }


    return returnBoolean
}