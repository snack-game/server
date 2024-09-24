package com.snackgame.server.game.snackgame.core.domain

import com.snackgame.server.game.snackgame.exception.InaccuratePercentileException
import kotlin.math.floor

class Percentile(private val percentile: Double) {

    init {
        validateRangeOf(percentile)
    }

    private fun validateRangeOf(percentile: Double) {
        if (percentile < 0 || percentile > 1) {
            throw InaccuratePercentileException(percentile)
        }
    }

    fun percentage(): Int {
        val percentageWise = floor(percentile * 100)
        return percentageWise.toInt()
    }
}
