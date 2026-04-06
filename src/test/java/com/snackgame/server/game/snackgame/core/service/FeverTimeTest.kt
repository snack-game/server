@file:Suppress("NonAsciiCharacters")


package com.snackgame.server.game.snackgame.core.service


import com.snackgame.server.game.snackgame.core.domain.item.FeverTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FeverTimeTest {

    @Test
    fun `피버타임은_시작_시점과_종료_시점_사이에_발생한_이벤트만_인정한다`() {
        val start = LocalDateTime.of(2025, 11, 30, 12, 0, 0)
        val end = start.plusSeconds(30)
        val feverTime = FeverTime(start, end)

        // 서버 시간 검증은 ±3분/5초 허용하므로, 각 occurredAt 근처로 serverNow 설정
        assertThat(feverTime.isFeverTime(start, start)).isTrue
        assertThat(feverTime.isFeverTime(end, end)).isTrue
        assertThat(feverTime.isFeverTime(start.plusSeconds(15), start.plusSeconds(15))).isTrue

        // 피버타임 범위 밖이면서, 서버 시간은 실제 요청 시점으로 설정
        assertThat(feverTime.isFeverTime(start.minusSeconds(2), start.minusSeconds(2))).isFalse
        assertThat(feverTime.isFeverTime(end.plusSeconds(2), end.plusSeconds(2))).isFalse
    }

    @Test
    fun `일시정지했다가_재개하면_피버_종료_시간이_정지했던_만큼_늘어난다`() {
        val start = LocalDateTime.of(2025, 11, 30, 12, 0, 0)
        val initialEnd = start.plusSeconds(30)
        val feverTime = FeverTime(start, initialEnd)


        val pausedAt = start.plusSeconds(10)
        feverTime.pause(pausedAt)


        val resumedAt = start.plusSeconds(20)
        feverTime.resume(resumedAt)


        assertThat(feverTime.feverEndAt).isEqualTo(initialEnd.plusSeconds(10))
    }

    @Test
    fun `일시정지_상태에서는_시간이_연장되지_않는다_재개해야_반영됨`() {
        val start = LocalDateTime.of(2025, 11, 30, 12, 0, 0)
        val feverTime = FeverTime(start, start.plusSeconds(30))

        feverTime.pause(start.plusSeconds(10))


        assertThat(feverTime.feverEndAt).isEqualTo(start.plusSeconds(30))
    }
}