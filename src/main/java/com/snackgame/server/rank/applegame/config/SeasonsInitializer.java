package com.snackgame.server.rank.applegame.config;

import static java.time.LocalDateTime.parse;

import java.util.List;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.snackgame.server.rank.applegame.domain.Season;
import com.snackgame.server.rank.applegame.domain.SeasonRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SeasonsInitializer {

    private final SeasonRepository seasonRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationStart() {
        if (seasonRepository.isEmpty()) {
            seasonRepository.saveAll(seasons());
        }
    }

    private List<Season> seasons() {
        return List.of(
                new Season(
                        "베타 시즌",
                        parse("2023-09-01T00:00:00"),
                        parse("2024-02-18T23:59:59")
                ),
                new Season(
                        "시즌 1",
                        parse("2024-02-19T00:00:00")
                )
        );
    }
}
