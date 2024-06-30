package com.snackgame.server.fixture;

import static java.time.LocalDateTime.parse;

import com.snackgame.server.rank.domain.Season;
import com.snackgame.server.support.fixture.FixtureSaver;

@SuppressWarnings("NonAsciiCharacters")
public class SeasonFixture {

    public static Season 베타시즌() {
        return new Season(
                "베타 시즌",
                parse("2023-09-01T00:00:00"),
                parse("2024-02-18T23:59:59"),
                1L
        );
    }

    public static Season 시즌1() {
        return new Season(
                "시즌 1",
                parse("2024-02-19T00:00:00"),
                null,
                2L
        );
    }

    public static void saveAll() {
        FixtureSaver.save(
                베타시즌(),
                시즌1()
        );
    }
}
