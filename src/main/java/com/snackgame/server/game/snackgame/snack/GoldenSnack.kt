package com.snackgame.server.game.snackgame.snack

import java.util.concurrent.ConcurrentHashMap

class GoldenSnack private constructor(number: Int) : Snack(number) {

    companion object {
        private val CACHE: MutableMap<Int, GoldenSnack> = ConcurrentHashMap()


        fun of(number: Int): GoldenSnack {
            return CACHE.computeIfAbsent(number) { GoldenSnack(it) }
        }
    }

    override fun golden(): GoldenSnack {
        return this
    }

    override fun isGolden(): Boolean {
        return true
    }

    override fun exists(): Boolean {
        return true
    }
}
