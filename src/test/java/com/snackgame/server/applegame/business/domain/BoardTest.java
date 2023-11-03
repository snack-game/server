package com.snackgame.server.applegame.business.domain;

import static com.snackgame.server.applegame.fixture.TestFixture.TWO_BY_FOUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.applegame.business.exception.AppleNotRemovableException;
import com.snackgame.server.applegame.business.exception.InvalidBoardSizeException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BoardTest {

    @Test
    void 게임판_높이는_1이상이어야_한다() {
        int height = 0;
        int width = 8;

        assertThatThrownBy(() -> new Board(height, width))
                .isInstanceOf(InvalidBoardSizeException.class)
                .hasMessage("잘못된 크기의 게임판입니다");
    }

    @Test
    void 게임판_너비는_1이상이어야_한다() {
        int height = 1;
        int width = 0;

        assertThatThrownBy(() -> new Board(height, width))
                .isInstanceOf(InvalidBoardSizeException.class)
                .hasMessage("잘못된 크기의 게임판입니다");
    }

    @Test
    void 원하는_크기의_게임판을_랜덤_생성한다() {
        int height = 4;
        int width = 8;

        var board = new Board(height, width);

        assertThat(board.getApples()).hasSize(height);
        assertThat(board.getApples()).allSatisfy(
                row -> assertThat(row).hasSize(width)
        );
    }

    @Test
    void 게임판을_초기화해도_같은_크기를_가진다() {
        int height = 4;
        int width = 8;
        var board = new Board(height, width);

        var reset = board.reset();

        assertThat(reset.getApples()).hasSize(height);
        assertThat(reset.getApples()).allSatisfy(
                row -> assertThat(row).hasSize(width)
        );
    }

    @Test
    void 게임판을_초기화하면_사과들이_바뀐다() {
        int height = 4;
        int width = 8;
        var board = new Board(height, width);

        var reset = board.reset();

        assertThat(board)
                .usingRecursiveComparison()
                .isNotEqualTo(reset);
    }

    @Test
    void 특정_범위의_사과들을_제거한다() {
        var board = TWO_BY_FOUR();
        var range = new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        );

        board.removeApplesIn(range);

        assertThat(range.getCompleteCoordinates()).allSatisfy(coordinate ->
                assertThat(board.getApples().get(coordinate.getY()).get(coordinate.getX()))
                        .isEqualTo(EmptyApple.get())
        );
    }

    @Test
    void 사과들의_합이_10이어야_제거할_수_있다() {
        var board = TWO_BY_FOUR();
        var range = new Range(
                new Coordinate(0, 0),
                new Coordinate(0, 1)
        );

        assertThatThrownBy(() -> board.removeApplesIn(range))
                .isInstanceOf(AppleNotRemovableException.class)
                .hasMessage("사과들의 합이 10이 아닙니다");
    }

    @Test
    void 제거된_사과들을_반환한다() {
        var board = TWO_BY_FOUR();
        var range = new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        );

        var removed = board.removeApplesIn(range);

        assertThat(removed).hasSize(2);
        assertThat(removed)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        PlainApple.of(9),
                        PlainApple.of(1)
                );
    }

    @Test
    void 하나를_황금사과로_만든다() {
        var board = new Board(10, 8);

        long goldenAppleCount = board.getApples().stream()
                .flatMap(Collection::stream)
                .filter(Apple::isGolden)
                .count();

        assertThat(goldenAppleCount).isOne();
    }
}
