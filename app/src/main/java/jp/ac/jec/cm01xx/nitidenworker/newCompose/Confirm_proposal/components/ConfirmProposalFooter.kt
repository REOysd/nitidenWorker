package jp.ac.jec.cm01xx.nitidenworker.newCompose.Confirm_proposal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm01xx.nitidenworker.R

@Composable
fun ConfirmProposalFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
    ) {
        ConfirmProposalFooterItem(
            titleText = "サービス内容の説明",
            contentText = "窓の外では桜が舞っているよ。今年の桜は特別きれいだね。病室のベッドに横たわりながら、これまでの人生を振り返っている私に、自然はこんなにも素晴らしい贈り物をくれた。\n" +
                    "あなたが生まれた日のことを、まるで昨日のように覚えている。小さな手を握った時の温もり、初めて目が合った時の喜び、そして何より、あなたの存在が私たちの人生をどれほど豊かにしてくれたか。\n" +
                    "医師から余命を告げられた時、最初は受け入れることができなかった。でも不思議なことに、時間が経つにつれて、むしろ残された時間の一瞬一瞬が愛おしく感じられるようになってきたの。\n" +
                    "今、あなたは素晴らしい大人になった。仕事に打ち込む姿、困っている人に手を差し伸べる優しさ、そして時には涙を見せる正直な心。母として、これ以上の誇りはないわ。\n" +
                    "ただ、最近のあなたは少し頑張りすぎているように見えるの。完璧を求めすぎて、自分を追い込んでいるように感じるわ。だから、これが私からの最後のアドバイスになるかもしれないけれど、いくつかの大切なことを伝えたいと思う。\n" +
                    "人生は完璧である必要はないの。むしろ、不完全だからこそ美しい。ちょうど、手作りのセーターの少しずれた編み目や、古い家具の傷跡のように。それらは全て、生きてきた証なのよ。"
        )
        Spacer(modifier = Modifier.height(24.dp))
        ConfirmProposalFooterItem(
            titleText = "検討している方への注意事項",
            contentText = "窓の外では桜が舞っているよ。今年の桜は特別きれいだね。病室のベッドに横たわりながら、これまでの人生を振り返っている私に、自然はこんなにも素晴らしい贈り物をくれた。\n" +
                    "あなたが生まれた日のことを、まるで昨日のように覚えている。小さな手を握った時の温もり、初めて目が合った時の喜び、そして何より、あなたの存在が私たちの人生をどれほど豊かにしてくれたか。\n" +
                    "医師から余命を告げられた時、最初は受け入れることができなかった。でも不思議なことに、時間が経つにつれて、むしろ残された時間の一瞬一瞬が愛おしく感じられるようになってきたの。\n" +
                    "今、あなたは素晴らしい大人になった。仕事に打ち込む姿、困っている人に手を差し伸べる優しさ、そして時には涙を見せる正直な心。母として、これ以上の誇りはないわ。\n" +
                    "ただ、最近のあなたは少し頑張りすぎているように見えるの。完璧を求めすぎて、自分を追い込んでいるように感じるわ。だから、これが私からの最後のアドバイスになるかもしれないけれど、いくつかの大切なことを伝えたいと思う。\n" +
                    "人生は完璧である必要はないの。むしろ、不完全だからこそ美しい。ちょうど、手作りのセーターの少しずれた編み目や、古い家具の傷跡のように。それらは全て、生きてきた証なのよ。"
        )

    }
}

@Composable
fun ConfirmProposalFooterItem(
    titleText: String,
    contentText: String
) {
    Text(
        text = titleText,
        style = TextStyle(
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.hiragino_bold)),
        ),
        modifier = Modifier
            .padding(start = 12.dp)
    )
    Spacer(modifier = Modifier.height(10.dp))
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(
                color = Color(0xFFEFEFEF),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Text(
            text = contentText,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.hiragino_medium))
            ),
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 12.dp)
        )
    }
}