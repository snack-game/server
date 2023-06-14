package com.snackgame.server.applegame.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CoordinatesTest {

    @Test
    void 좌상단을_알_수_있다() {
        var range = new Coordinates(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 2),
                new Coordinate(0, 3),
                new Coordinate(0, 4),
                new Coordinate(1, 4),
                new Coordinate(1, 3)
        ));

        assertThat(range.getTopLeft()).isEqualTo(new Coordinate(0, 1));
    }

    @Test
    void 우하단을_알_수_있다() {
        var range = new Coordinates(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 2),
                new Coordinate(0, 3),
                new Coordinate(0, 4),
                new Coordinate(1, 2),
                new Coordinate(1, 3)
        ));

        assertThat(range.getBottomRight()).isEqualTo(new Coordinate(1, 4));
    }
}
