package com.snackgame.server.member.service

import com.snackgame.server.member.domain.Name
import org.springframework.stereotype.Component

@Component
class SocialMemberNameRandomizer {

    fun getName(): Name {
        return Name(getRandomizedPureWord() + "_스낵이")
    }

    companion object {

        private val adjectives: Array<String> = arrayOf(
            "가냘픈", "고즈넉한", "그윽한", "깨끗한", "나긋나긋한", "느긋한", "다정한", "맑은", "따분한", "밝은", "보드라운", "상큼한", "산뜻한",
            "섬세한", "수줍은", "순수한", "신비로운", "싱그러운", "쓸쓸한", "아름다운", "어여쁜", "우아한", "은은한", "잔잔한", "정다운", "정겨운",
            "조용한", "청초한", "달보드레한", "이든", "미쁜", "소담한", "하르르한"
        )

        private val determiners: Array<String> = arrayOf(
            "시나브로", "안다미로", "아스라이", "한가로이", "애오라지", "모람모람", "가끔", "매우", "엄청", "갑자기", "다르르", "도담히", "도도히",
            "도르르", "돌돌히", "듬직이", "또르르", "말꼼히", "맹맹히", "뭉근히", "바르르", "보윰히", "볼그레", "볼록이", "살그래", "생생히",
            "소르르", "어정쩡", "올올이", "흐뭇이", "헝그레"

        )

    }

    private fun getRandomizedPureWord(): String {
        val adjective = adjectives[(adjectives.indices).random()]
        val determiner = determiners[(determiners.indices).random()]

        return determiner + "_" + adjective
    }
}