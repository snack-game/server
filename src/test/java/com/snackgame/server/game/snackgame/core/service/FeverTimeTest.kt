@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.snackgame.core.service

import com.snackgame.server.game.snackgame.core.domain.item.FeverTime
import com.snackgame.server.game.snackgame.exception.InvalidStreakTimeException
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FeverTimeTest {

    @Test
    fun `일시정지가 되면 피버타임이 비활성화 된다`() {
        val startTime = LocalDateTime.now()
        val pauseTime = startTime.plusSeconds(10)
        val fever = FeverTime.start(startTime)

        fever.pause(pauseTime)

        assertFalse(fever.isActive(pauseTime))
    }

    @Test
    fun `피버타임을 재개하면 다시 시간을 재야한다`() {
        val startTime = LocalDateTime.now()
        val pauseTime = startTime.plusSeconds(10)
        val resumeTime = pauseTime.plusSeconds(5)
        val fever = FeverTime.start(startTime)

        fever.pause(pauseTime)
        fever.resume(resumeTime)

        assertTrue(fever.isActive(resumeTime))
    }

    @Test
    fun `isActive should return false after fever ends`() {
        val startTime = LocalDateTime.of(2025, 9, 8, 8, 0, 0)
        val endTime = startTime.plusSeconds(31)
        val fever = FeverTime.start(startTime)

        assertFalse(fever.isActive(endTime))
    }

    @Test
    fun `validateFeverStreakOccurredAt should correctly validate client occurrence`() {
        val startTime = LocalDateTime.of(2025, 9, 8, 8, 0, 0)
        val fever = FeverTime.start(startTime)

        val validTime = startTime.plusSeconds(10)
        val invalidTime = startTime.plusSeconds(40)

        assertTrue(fever.validateFeverStreakOccurredAt(validTime))
        assertThrows(InvalidStreakTimeException::class.java) { fever.validateFeverStreakOccurredAt(invalidTime) }
    }

    @Test
    fun `pause and resume multiple times should correctly update remaining time`() {
        val startTime = LocalDateTime.of(2025, 9, 8, 8, 0, 0)
        val fever = FeverTime.start(startTime)

        val pause1 = startTime.plusSeconds(10)
        val resume1 = pause1.plusSeconds(5)
        val pause2 = resume1.plusSeconds(5)
        val resume2 = pause2.plusSeconds(3)

        fever.pause(pause1)
        fever.resume(resume1)
        fever.pause(pause2)
        fever.resume(resume2)

        val checkTime = resume2.plusSeconds(10)
        assertTrue(fever.isActive(checkTime)) // 남은 시간 고려
    }
}
