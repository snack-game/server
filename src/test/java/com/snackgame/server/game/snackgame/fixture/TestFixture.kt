package com.snackgame.server.game.snackgame.fixture

import com.snackgame.server.game.snackgame.core.domain.Board
import com.snackgame.server.game.snackgame.core.domain.snack.EmptySnack
import com.snackgame.server.game.snackgame.core.domain.snack.GoldenSnack
import com.snackgame.server.game.snackgame.core.domain.snack.PlainSnack

object TestFixture {

    const val TWO_BY_FOUR_AS_JSON =
        "{\"snacks\":[[{\"number\":1,\"isGolden\":false},{\"number\":1,\"isGolden\":false},{\"number\":0,\"isGolden\":false},{\"number\":5,\"isGolden\":false}],[{\"number\":9,\"isGolden\":false},{\"number\":2,\"isGolden\":false},{\"number\":0,\"isGolden\":false},{\"number\":2,\"isGolden\":false}]]}"
    const val TWO_BY_TWO_WITH_GOLDEN_SNACK_AS_JSON =
        "{\"snacks\":[[{\"number\":1,\"isGolden\":false},{\"number\":8,\"isGolden\":false}],[{\"number\":9,\"isGolden\":true},{\"number\":2,\"isGolden\":false}]]}"

    /**
     * 다음과 같은 보드를 생성합니다:
     *
     * `1`, `1`, `_`, `5`
     *
     * `9`, `2`, `_`, `2`
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
     * 다음과 같은 보드를 생성합니다:
     *
     * `1`, `1`, `_`, `5`
     *
     * `9`, `2`, `_`, `2`
     *
     * `1`, `1`, `3`, `5`
     */
    fun THREE_BY_FOUR(): Board {
        return Board(
            arrayListOf(
                arrayListOf(PlainSnack.of(1), PlainSnack.of(1), EmptySnack.get(), PlainSnack.of(5)),
                arrayListOf(PlainSnack.of(9), PlainSnack.of(2), EmptySnack.get(), PlainSnack.of(2)),
                arrayListOf(PlainSnack.of(1), PlainSnack.of(1), PlainSnack.of(3), PlainSnack.of(5))
            )
        )
    }

    /**
     * 다음과 같은 보드를 생성합니다.
     * 9는 황금 사과입니다
     *
     * `1`, `8`
     *
     * **`(9)`**, `2`
     */
    fun TWO_BY_TWO_WITH_GOLDEN_APPLE(): Board {
        return Board(
            arrayListOf(
                arrayListOf(PlainSnack.of(1), PlainSnack.of(8)),
                arrayListOf(GoldenSnack.of(9), PlainSnack.of(2))
            )
        )
    }
}
