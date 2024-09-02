package com.snackgame.server.game.snackgame.fixture

import com.snackgame.server.game.snackgame.domain.Board
import com.snackgame.server.game.snackgame.snack.EmptySnack
import com.snackgame.server.game.snackgame.snack.PlainSnack

object TestFixture {

    const val TWO_BY_FOUR_AS_JSON =
        "{\"snacks\":[[{\"number\":1,\"isGolden\":false},{\"number\":1,\"isGolden\":false},{\"number\":0,\"isGolden\":false},{\"number\":5,\"isGolden\":false}],[{\"number\":9,\"isGolden\":false},{\"number\":2,\"isGolden\":false},{\"number\":0,\"isGolden\":false},{\"number\":2,\"isGolden\":false}]]}"
    const val TWO_BY_TWO_WITH_GOLDEN_SNACK_AS_JSON =
        "{\"snacks\":[[{\"number\":1,\"isGolden\":false},{\"number\":8,\"isGolden\":false}],[{\"number\":9,\"isGolden\":true},{\"number\":2,\"isGolden\":false}]]}"

    /**
     * <p>[1, 1, _, 5]</p>
     * <p>[9, 2, _, 2]</p>
     */
    fun TWO_BY_FOUR(): Board {
        return Board(
            arrayListOf(
                arrayListOf(PlainSnack.of(1), PlainSnack.of(1), EmptySnack.get(), PlainSnack.of(5)),
                arrayListOf(PlainSnack.of(9), PlainSnack.of(2), EmptySnack.get(), PlainSnack.of(2))
            )
        )
    }

    /**
     * <p>[1, 1]</p>
     * <p>[<b>9</b>, 2]</p>
     * 9는 황금사과이다.
     */
    fun TWO_BY_TWO_WITH_GOLDEN_APPLE(): Board {
        return Board(
            arrayListOf(
                arrayListOf(PlainSnack.of(1), PlainSnack.of(8)),
                arrayListOf(PlainSnack.of(9).golden(), PlainSnack.of(2))
            )
        )
    }
}
