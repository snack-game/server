package com.snackgame.server.history.fixture

import com.snackgame.server.game.snackgame.core.domain.Snackgame
import com.snackgame.server.game.snackgame.fixture.BoardFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.support.fixture.FixtureSaver
import java.time.Duration

object SnackgameFixture {

    fun first() = Snackgame(
        땡칠().id,
        BoardFixture.TWO_BY_FOUR(),
        Duration.ofDays(7).negated(),
        100
    )

    fun second() = Snackgame(
        땡칠().id,
        BoardFixture.TWO_BY_TWO_WITH_GOLDEN_SNACK(),
        Duration.ofDays(6).negated(),
        200
    )

    fun secondBest() = Snackgame(
        땡칠().id,
        BoardFixture.TWO_BY_FOUR(),
        Duration.ofDays(6).negated(),
        250
    )

    fun third() = Snackgame(
        땡칠().id,
        BoardFixture.TWO_BY_FOUR(),
        Duration.ofDays(5).negated(),
        300
    )

    fun fourth() = Snackgame(
        땡칠().id,
        BoardFixture.TWO_BY_FOUR(),
        Duration.ofDays(4).negated(),
        400
    )

    fun fifth() = Snackgame(
        땡칠().id,
        BoardFixture.TWO_BY_FOUR(),
        Duration.ofDays(3).negated(),
        500
    )

    fun sixth() = Snackgame(
        땡칠().id,
        BoardFixture.TWO_BY_FOUR(),
        Duration.ofDays(2).negated(),
        600
    )

    fun sixthBest() = Snackgame(
        땡칠().id,
        BoardFixture.TWO_BY_FOUR(),
        Duration.ofDays(2).negated(),
        650
    )

    fun seventh() = Snackgame(
        땡칠().id,
        BoardFixture.TWO_BY_TWO_WITH_GOLDEN_SNACK(),
        Duration.ofDays(1).negated(),
        700
    )

    fun eighth() = Snackgame(
        땡칠().id,
        BoardFixture.TWO_BY_TWO_WITH_GOLDEN_SNACK(),
        Duration.ZERO,
        800
    )

    fun saveAll() {
        FixtureSaver.save(
            first(),
            second(),
            secondBest(),
            third(),
            fourth(),
            fifth(),
            sixth(),
            sixthBest(),
            seventh(),
            eighth()
        )
    }
}
