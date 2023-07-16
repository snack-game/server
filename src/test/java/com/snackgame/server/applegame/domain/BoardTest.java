package com.snackgame.server.applegame.domain;

import static com.snackgame.server.applegame.fixture.TestFixture.TWO_BY_FOUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.applegame.business.domain.Apple;
import com.snackgame.server.applegame.business.domain.Board;
import com.snackgame.server.applegame.business.domain.Coordinate;
import com.snackgame.server.applegame.business.domain.Range;
import com.snackgame.server.applegame.business.domain.exception.AppleNotRemovableException;
import com.snackgame.server.applegame.business.domain.exception.InvalidRangeException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BoardTest {

    @Test
    void 원하는_크기의_게임판을_랜덤_생성한다() {
        int height = 4;
        int width = 8;

        var board = Board.ofRandomized(height, width);

        assertThat(board.getApples()).hasSize(height);
        assertThat(board.getApples()).allSatisfy(
                row -> assertThat(row).hasSize(width)
        );
    }

    @Test
    void 특정_범위의_사과들을_제거한다() {
        var board = TWO_BY_FOUR();
        var range = new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        ));

        board.removeApplesIn(range);

        assertThat(range.getAppleCoordinates()).allSatisfy(coordinate ->
                assertThat(board.getApples().get(coordinate.getY()).get(coordinate.getX()))
                        .isEqualTo(Apple.EMPTY)
        );
    }

    @Test
    void 사과_좌표들에_사과가_없으면_예외를_던진다() {
        var board = TWO_BY_FOUR();
        var range = new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 2),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        ));

        assertThatThrownBy(() -> board.removeApplesIn(range))
                .isInstanceOf(InvalidRangeException.class);
    }

    @Test
    void 사과_좌표들_외에_사과가_있으면_예외를_던진다() {
        var board = TWO_BY_FOUR();
        var range = new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 3)
        ));

        assertThatThrownBy(() -> board.removeApplesIn(range))
                .isInstanceOf(InvalidRangeException.class);
    }

    @Test
    void 사과들의_합이_10이어야_제거할_수_있다() {
        var board = TWO_BY_FOUR();
        var range = new Range(List.of(
                new Coordinate(0, 0),
                new Coordinate(0, 1))
        );

        assertThatThrownBy(() -> board.removeApplesIn(range))
                .isInstanceOf(AppleNotRemovableException.class)
                .hasMessage("사과들의 합이 10이 아닙니다");
    }

    @Test
    void 제거된_사과들의_개수를_반환한다() {
        var board = TWO_BY_FOUR();
        var range = new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        ));

        assertThat(board.removeApplesIn(range)).isEqualTo(4);
    }
}
