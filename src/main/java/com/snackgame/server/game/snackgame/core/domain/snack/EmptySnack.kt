package com.snackgame.server.game.snackgame.core.domain.snack

import com.snackgame.server.game.snackgame.exception.EmptySnackException

class EmptySnack private constructor() : Snack() {

    override fun golden(): GoldenSnack {
        throw EmptySnackException("빈 사과를 바꿀 수 없습니다")
    }

    override fun isGolden(): Boolean {
        return false
    }

    override fun exists(): Boolean {
        return false
    }

    companion object {
        private val INSTANCE: EmptySnack = EmptySnack()

        fun get(): EmptySnack {
            return INSTANCE
        }
    }
}
