package com.snackgame.server.member.domain

import com.snackgame.server.member.exception.NameLengthException
import java.util.regex.Pattern
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Name(
    @Column(name = "name")
    val string: String
) {

    init {
        validateLengthOf(string)
    }

    private fun validateLengthOf(string: String) {
        if (string.length < 2 || string.length > 13) {
            throw NameLengthException()
        }
    }

    fun nextAvailable(): Name {
        if (Pattern.matches(NUMBERED_PATTERN, string)) {
            val currentNumber = string.substringAfterLast('_').toLong()
            return Name("${string.substringBeforeLast('_')}_${currentNumber + 1}")
        }
        return Name(string + "_2")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Name

        return string == other.string
    }

    override fun hashCode(): Int {
        return string.hashCode()
    }

    companion object {
        private const val NUMBERED_PATTERN = ".+_\\d+"
    }
}
