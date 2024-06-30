package com.snackgame.server.rank.config

import com.snackgame.server.rank.domain.Season
import com.snackgame.server.rank.domain.SeasonRepository
import lombok.RequiredArgsConstructor
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@RequiredArgsConstructor
@Component
class SeasonsInitializer(private val seasonRepository: SeasonRepository) {


    @EventListener(ContextRefreshedEvent::class)
    fun onApplicationStart() {
        if (seasonRepository.isEmpty) {
            seasonRepository.saveAll(seasons())
        }
    }

    private fun seasons(): List<Season> {
        return listOf(
            Season(
                "베타 시즌",
                LocalDateTime.parse("2023-09-01T00:00:00"),
                LocalDateTime.parse("2024-02-18T23:59:59")
            ),
            Season(
                "시즌 1",
                LocalDateTime.parse("2024-02-19T00:00:00")
            )
        )
    }
}
