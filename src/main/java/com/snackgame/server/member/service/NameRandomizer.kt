package com.snackgame.server.member.service

import com.snackgame.server.member.domain.Name
import org.springframework.stereotype.Component

@Component
class NameRandomizer {
    fun getBy(prefix: String): Name {
        return Name(prefix + "_" + getRandomizedAlphabets(RANDOMIZED_LENGTH))
    }

    companion object {
        private const val RANDOMIZED_LENGTH = 10
        private const val ALPHABET_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

        private fun getRandomizedAlphabets(length: Int): String {
            val randomized = StringBuilder()
            repeat(length) {
                randomized.append(ALPHABET_POOL.random())
            }
            return randomized.toString()
        }
    }
}
