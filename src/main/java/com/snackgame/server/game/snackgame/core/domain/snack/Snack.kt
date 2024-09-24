package com.snackgame.server.game.snackgame.core.domain.snack

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.snackgame.server.game.snackgame.exception.SnackNumberRangeException

@JsonDeserialize(using = SnackDeserializer::class)
abstract class Snack protected constructor(
) {
    private var number: Int = 0

    protected companion object {
        const val NUMBER_MINIMUM = 1
        const val NUMBER_MAXIMUM = 9
    }

    constructor(number: Int) : this() {
        validateRangeOf(number)
        this.number = number
    }

    private fun validateRangeOf(number: Int) {
        if (number !in NUMBER_MINIMUM..NUMBER_MAXIMUM) {
            throw SnackNumberRangeException()
        }
    }

    abstract fun golden(): GoldenSnack

    @JsonProperty("isGolden")
    abstract fun isGolden(): Boolean

    abstract fun exists(): Boolean

    fun getNumber(): Int {
        return number
    }


}
