package com.snackgame.server.applegame.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RangeTest {

    @Test
    void 범위_내의_모든_좌표들을_알_수_있다() {
        var range = new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        );
        var completeCoordinatesInRange = List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 2),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 2),
                new Coordinate(1, 3)
        );

        assertThat(range.getCompleteCoordinates()).containsExactlyElementsOf(completeCoordinatesInRange);
    }
}
