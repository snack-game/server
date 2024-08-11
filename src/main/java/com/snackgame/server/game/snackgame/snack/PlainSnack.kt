package com.snackgame.server.game.snackgame.snack

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadLocalRandom

class PlainSnack private constructor(number: Int) : Snack(number) {

    companion object {
        private val CACHE: MutableMap<Int, PlainSnack> = ConcurrentHashMap()

        fun of(number: Int): PlainSnack {
            return CACHE.computeIfAbsent(number) { PlainSnack(it) }
        }

        fun ofRandomizedNumber(): PlainSnack {
            val randomizedNumber = ThreadLocalRandom.current().nextInt(NUMBER_MINIMUM, NUMBER_MAXIMUM + 1)
            return of(randomizedNumber)
        }
    }

    override fun golden(): GoldenSnack {
        return GoldenSnack.of(getNumber())
    }

    override fun isGolden(): Boolean {
        return false
    }

    override fun exists(): Boolean {
        return true
    }
}