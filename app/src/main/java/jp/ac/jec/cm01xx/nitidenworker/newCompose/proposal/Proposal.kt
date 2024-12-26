package jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ac.jec.cm01xx.nitidenworker.newCompose.common.CommonTopBar
import jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents.ProposalConfirmButton
import jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents.ProposalImagePicker
import jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents.ProposalItemDropdownMenu
import jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents.ProposalItemTextField
import jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents.ProposalItemTitle
import jp.ac.jec.cm01xx.nitidenworker.newCompose.proposal.conponents.ProposalMoviePicker

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Proposal() {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var expanded by rememberSaveable { mutableStateOf(false) }
    val categories = listOf("デザイン","プログラミング","企画","ゲームプログラミング")
    var categoryText by rememberSaveable { mutableStateOf("") }
    var categoryTextIsError by rememberSaveable { mutableStateOf(true) }
    var titleText by rememberSaveable { mutableStateOf("") }
    var titleTextIsError by rememberSaveable { mutableStateOf(true) }
    var isTitleTextFocused by remember { mutableStateOf(true) }
    var subTitleText by rememberSaveable { mutableStateOf("") }
    var isSubTitleTextFocused by remember { mutableStateOf(true) }
    var descriptionText by rememberSaveable { mutableStateOf("") }
    var descriptionTextIsError by rememberSaveable { mutableStateOf(true) }
    var isDescriptionTextFocused by remember { mutableStateOf(true) }
    var expectedDurationText by rememberSaveable { mutableStateOf("") }
    var expectedDurationTextIsError by rememberSaveable { mutableStateOf(true) }
    var isExpectedDurationTextFocused by remember { mutableStateOf(true) }
    var cautionsText by rememberSaveable { mutableStateOf("") }
    var isCautionsTextFocused by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current

    val imageSelectPageCount = 5
    val imageSelectPagerState = rememberPagerState(
        pageCount = {imageSelectPageCount},
        initialPage = 0
    )
    val currentImageSelectPage = imageSelectPagerState.currentPage
    var selectImages by rememberSaveable{
        mutableStateOf(List(imageSelectPageCount) { null as Uri? })
    }
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let{
                selectImages = selectImages.toMutableList().also { it[currentImageSelectPage] = uri }
            }
        }
    )
    var selectMovie by rememberSaveable { mutableStateOf<Uri?>(null) }
    val moviePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                selectMovie = uri
            }
        }
    )
    fun onSelectImage() {
        photoPicker.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
    fun onDeleteImage(currentImageSelectPage:Int) {
        selectImages = selectImages.toMutableList()
            .also { it[currentImageSelectPage] = null }
    }
    fun onSelectMovie() {
        moviePicker.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.VideoOnly
            )
        )
    }
    fun onDeleteMovie() {
        selectMovie = null
    }


    Scaffold(
        topBar = {
            CommonTopBar(
                showBackButton = true,
                topAppBarTitle = "提案作成画面",
                onClickToBack = {  },
                isCenterAlignment = true
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                        }
                    )
                }
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            ProposalItemTitle(
                itemTitle = "カテゴリー",
                isShowRequired = true,
            )
            ProposalItemDropdownMenu(
                expanded = expanded,
                onExpandedChange = {expanded = it},
                placeholder = "カテゴリーを選択してください",
                itemText = categoryText,
                isShowItemError = categoryTextIsError,
                onChangeItemError = {categoryTextIsError = it},
                onChangeItemText = {categoryText = it},
                categories = categories,
            )
            Spacer(modifier = Modifier.height(32.dp))
            ProposalItemTitle(
                itemTitle = "タイトル",
                isShowRequired = true
            )
            ProposalItemTextField(
                itemText = titleText,
                placeholder = "タイトルを入力してください",
                itemErrorText = "タイトルが入力されていません",
                maxTextLength = 30,
                maxLines = 1,
                onChangeItemText = {titleText = it},
                onChangeItemError = {titleTextIsError = it},
                onChangeItemFocus = {isTitleTextFocused = it},
                isShowItemError = titleTextIsError,
                isItemFocused = isTitleTextFocused,
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            ProposalItemTitle(
                itemTitle = "サブタイトル（タイトルを補足説明）",
                isShowRequired = false
            )
            ProposalItemTextField(
                itemText = subTitleText,
                placeholder = "サブタイトルを入力してください",
                maxTextLength = 60,
                maxLines = 2,
                onChangeItemText = {subTitleText = it},
                onChangeItemFocus = {isSubTitleTextFocused = it},
                isItemFocused = isSubTitleTextFocused,
                modifier = Modifier
                    .heightIn(min = 50.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            ProposalItemTitle(
                itemTitle = "サービス内容の説明",
                isShowRequired = true
            )
            ProposalItemTextField(
                itemText = descriptionText,
                placeholder = "提供できるサービス内容の説明を入力してください",
                itemErrorText = "サービス内容が入力されていません",
                maxTextLength = 1000,
                maxLines = Int.MAX_VALUE,
                onChangeItemText = {descriptionText = it},
                onChangeItemError = {descriptionTextIsError = it},
                onChangeItemFocus = {isDescriptionTextFocused = it},
                isShowItemError = descriptionTextIsError,
                isItemFocused = isDescriptionTextFocused,
                modifier = Modifier
                    .heightIn(min = 200.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            ProposalItemTitle(
                itemTitle = "予想完成期間",
                isShowRequired = true
            )
            ProposalItemTextField(
                itemText = expectedDurationText,
                placeholder = "128",
                itemErrorText = "予想完成期間が入力されていません",
                suffix = "日",
                maxTextLength = 3,
                maxLines = 1,
                keyboardType = KeyboardType.Number,
                onChangeItemText = {expectedDurationText = it},
                onChangeItemError = {expectedDurationTextIsError = it},
                onChangeItemFocus = {isExpectedDurationTextFocused = it},
                isShowItemError = expectedDurationTextIsError,
                isItemFocused = isExpectedDurationTextFocused,
                isShowItemWordCount = false,
                modifier = Modifier
                    .width(124.dp)
                    .height(50.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            ProposalItemTitle(
                itemTitle = "検討している方への注意事項",
                isShowRequired = false
            )
            ProposalItemTextField(
                itemText = cautionsText,
                placeholder = "購入を検討しているユーザーへの注意事項を入力してください",
                maxTextLength = 500,
                maxLines = Int.MAX_VALUE,
                onChangeItemText = {cautionsText = it},
                onChangeItemFocus = {isCautionsTextFocused = it},
                isItemFocused = isCautionsTextFocused,
                modifier = Modifier
                    .heightIn(min = 200.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
            ProposalItemTitle(
                itemTitle = "画像サンプル",
                itemSubTitle = "(最大5つ)",
                isShowRequired = false
            )
            ProposalImagePicker(
                imageSelectPagerState = imageSelectPagerState,
                selectImages = selectImages,
                imageSelectPageCount = imageSelectPageCount,
                onDeleteImage = { onDeleteImage(it) },
                onSelectImage = { onSelectImage() }
            )
            Spacer(modifier = Modifier.height(32.dp))
            ProposalItemTitle(
                itemTitle = "画像サンプル",
                itemSubTitle = "(最大1つ)",
                isShowRequired = false
            )
            ProposalMoviePicker(
                selectMovie = selectMovie,
                onDeleteMovie = { onDeleteMovie() },
                onSelectMovie = { onSelectMovie() }
            )
            Spacer(modifier = Modifier.height(60.dp))
            ProposalConfirmButton(
                scope = scope,
                scrollState = scrollState
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProposal() {
    Proposal()
}