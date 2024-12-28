package jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal.components.ConfirmProposalBody
import jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal.components.ConfirmProposalFooter
import jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal.components.ConfirmProposalHeader
import jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal.components.ConfirmProposalPublishButton
import jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal.components.ConfirmProposalThumbnail
import jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal.components.ConfirmProposalUserInformationButton
import jp.ac.jec.cm01xx.nitidenworker.newCompose.common.CommonTopBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConfirmProposal() {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    var isLikeButtonSelected by rememberSaveable { mutableStateOf(false) }
    var isFavoriteButtonSelected by rememberSaveable { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(0) }
    var favoriteCount by remember { mutableIntStateOf(0) }
    //sample
    val sampleMedia = listOf(
        // 画像サンプル（Picsum）
        "https://picsum.photos/400/300",
        "https://picsum.photos/400/300?random=1",
        "https://picsum.photos/400/300?random=2",

        // 無料サンプル動画（Pixabay）
        "https://cdn.pixabay.com/vimeo/477127035/wave-71315.mp4",
        "https://cdn.pixabay.com/vimeo/190566437/ocean-7242.mp4",

        // 別の画像ソース（Unsplash）
        "https://source.unsplash.com/random/400x300",
        "https://source.unsplash.com/random/400x300?nature",
        "https://source.unsplash.com/random/400x300?city",

        // サンプル動画（一般的なテスト用）
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
    )
    //sample
    val emptyList = emptyList<String?>()
    //sample
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { sampleMedia.size }
    )

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
        ) {
            ConfirmProposalThumbnail(
                scope = scope,
                selectedImageAndMovie = sampleMedia,
                selectedImageAndMoviePagerState = pagerState
            )
            ConfirmProposalHeader(
                proposalTitleText = "「習慣の力で人生を変える 明日から1%」",
                proposalSubTitleText = "人生を変える最小習慣 ～科学的アプローチで証明された、" +
                        "成功者たちが密かに実践する意志力に頼らない27の行動習慣と、その驚くべき成果～",
                isLikeButtonSelected = isLikeButtonSelected,
                isFavoriteButtonSelected = isFavoriteButtonSelected,
                likeCount = likeCount,
                favoriteCount = favoriteCount,
                onLikeClick = {
                    isLikeButtonSelected = !isLikeButtonSelected
                    if (isLikeButtonSelected) likeCount++ else likeCount--
                },
                onFavoriteClick = {
                    isFavoriteButtonSelected = !isFavoriteButtonSelected
                    if (isFavoriteButtonSelected) favoriteCount++ else favoriteCount--
                }
            )
            ConfirmProposalUserInformationButton()
            ConfirmProposalBody(
                categoryText = "モバイルアプリケーション開発科",
                expectedCompleteDateText = 30,
                appliedUserCountText = 10
            )
            ConfirmProposalFooter()
            ConfirmProposalPublishButton()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfirm_proposal() {
    ConfirmProposal()
}
