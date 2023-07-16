package com.snackgame.server.applegame.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.applegame.business.domain.Coordinate;
import com.snackgame.server.applegame.business.domain.Range;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RangeTest {

    @Test
    void 좌상단을_알_수_있다() {
        var range = new Range(List.of(
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
        var range = new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 2),
                new Coordinate(0, 3),
                new Coordinate(0, 4),
                new Coordinate(1, 2),
                new Coordinate(1, 3)
        ));

        assertThat(range.getBottomRight()).isEqualTo(new Coordinate(1, 4));
    }

    @Test
    void 사과_좌표들을_알_수_있다() {
        var appleCoordinates = List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        );
        var range = new Range(appleCoordinates);

        assertThat(range.getAppleCoordinates()).containsExactlyElementsOf(appleCoordinates);
    }

    @Test
    void 범위_내의_모든_좌표들을_알_수_있다() {
        var range = new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        ));
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

    @Test
    void 빈_좌표들을_알_수_있다() {
        var range = new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        ));
        var emptyCoordinatesInRange = List.of(
                new Coordinate(0, 2),
                new Coordinate(1, 2)
        );

        assertThat(range.getEmptyCoordinates()).containsExactlyElementsOf(emptyCoordinatesInRange);
    }
}
