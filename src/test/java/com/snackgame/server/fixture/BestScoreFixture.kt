@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.fixture

import com.snackgame.server.game.metadata.Metadata
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.member.fixture.MemberFixture.똥수
import com.snackgame.server.member.fixture.MemberFixture.유진
import com.snackgame.server.member.fixture.MemberFixture.정언
import com.snackgame.server.member.fixture.MemberFixture.정환
import com.snackgame.server.rank.domain.BestScore
import com.snackgame.server.support.fixture.FixtureSaver

object BestScoreFixture {
    @JvmStatic
    fun 사과게임_베타시즌_똥수_10점(): BestScore {
        return BestScore(똥수().id, Metadata.APPLE_GAME.gameId, SeasonFixture.베타시즌().id, 1, 10, id = 1)
    }

    @JvmStatic
    fun 사과게임_베타시즌_땡칠_10점(): BestScore {
        return BestScore(땡칠().id, Metadata.APPLE_GAME.gameId, SeasonFixture.베타시즌().id, 2, 10, id = 2)
    }

    @JvmStatic
    fun 사과게임_베타시즌_정환_8점(): BestScore {
        return BestScore(정환().id, Metadata.APPLE_GAME.gameId, SeasonFixture.베타시즌().id, 3, 8, id = 3)
    }

    @JvmStatic
    fun 사과게임_베타시즌_유진_6점(): BestScore {
        return BestScore(유진().id, Metadata.APPLE_GAME.gameId, SeasonFixture.베타시즌().id, 4, 6, id = 4)
    }

    @JvmStatic
    fun 사과게임_베타시즌_정언_8점(): BestScore {
        return BestScore(정언().id, Metadata.APPLE_GAME.gameId, SeasonFixture.베타시즌().id, 5, 8, id = 5)
    }

    @JvmStatic
    fun 사과게임_시즌1_땡칠_20점(): BestScore {
        return BestScore(땡칠().id, Metadata.APPLE_GAME.gameId, SeasonFixture.시즌1().id, 6, 20, id = 6)
    }

    @JvmStatic
    fun 사과게임_시즌1_정환_20점(): BestScore {
        return BestScore(정환().id, Metadata.APPLE_GAME.gameId, SeasonFixture.시즌1().id, 7, 20, id = 7)
    }

    @JvmStatic
    fun 사과게임_시즌1_유진_20점(): BestScore {
        return BestScore(유진().id, Metadata.APPLE_GAME.gameId, SeasonFixture.시즌1().id, 8, 20, id = 8)
    }

    @JvmStatic
    fun 사과게임_시즌1_정언_8점(): BestScore {
        return BestScore(정언().id, Metadata.APPLE_GAME.gameId, SeasonFixture.시즌1().id, 9, 8, id = 9)
    }

    @JvmStatic
    fun 스낵게임_시즌1_땡칠_20점(): BestScore {
        return BestScore(땡칠().id, Metadata.SNACK_GAME.gameId, SeasonFixture.시즌1().id, 11, 20, id = 10)
    }

    @JvmStatic
    fun 스낵게임_시즌1_유진_20점(): BestScore {
        return BestScore(유진().id, Metadata.SNACK_GAME.gameId, SeasonFixture.시즌1().id, 10, 20, id = 11)
    }

    @JvmStatic
    fun 스낵게임_시즌1_정언_8점(): BestScore {
        return BestScore(정언().id, Metadata.SNACK_GAME.gameId, SeasonFixture.시즌1().id, 13, 8, id = 13)
    }

    @JvmStatic
    fun saveAll() {
        MemberFixture.saveAll()
        SeasonFixture.saveAll()
        FixtureSaver.save(
            사과게임_베타시즌_똥수_10점(),
            사과게임_베타시즌_땡칠_10점(),
            사과게임_베타시즌_정환_8점(),
            사과게임_베타시즌_유진_6점(),
            사과게임_베타시즌_정언_8점(),
            사과게임_시즌1_땡칠_20점(),
            사과게임_시즌1_정환_20점(),
            사과게임_시즌1_유진_20점(),
            사과게임_시즌1_정언_8점(),
            스낵게임_시즌1_땡칠_20점(),
            스낵게임_시즌1_유진_20점(),
            스낵게임_시즌1_정언_8점()
        )
    }
}
