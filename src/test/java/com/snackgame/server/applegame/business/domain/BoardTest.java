package com.snackgame.server.applegame.business.domain;

import static com.snackgame.server.applegame.fixture.TestFixture.TWO_BY_FOUR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collection;
import java.util.List;

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
        var coordinates = List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        );

        board.removeApplesIn(coordinates);

        assertThat(coordinates).allSatisfy(coordinate ->
                assertThat(board.getApples().get(coordinate.getY()).get(coordinate.getX()))
                        .isEqualTo(Apple.EMPTY)
        );
    }

    @Test
    void 사과_좌표들에_사과가_없으면_예외를_던진다() {
        var board = TWO_BY_FOUR();
        var coordinates = List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 2),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        );

        assertThatThrownBy(() -> board.removeApplesIn(coordinates))
                .isInstanceOf(AppleNotRemovableException.class)
                .hasMessage("없는 사과를 제거하려고 했습니다");
    }

    @Test
    void 사과들의_합이_10이어야_제거할_수_있다() {
        var board = TWO_BY_FOUR();
        var coordinates = List.of(
                new Coordinate(0, 0),
                new Coordinate(0, 1)
        );

        assertThatThrownBy(() -> board.removeApplesIn(coordinates))
                .isInstanceOf(AppleNotRemovableException.class)
                .hasMessage("사과들의 합이 10이 아닙니다");
    }

    @Test
    void 제거된_사과들의_개수를_반환한다() {
        var board = TWO_BY_FOUR();
        var coordinates = List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        );

        assertThat(board.removeApplesIn(coordinates)).isEqualTo(4);
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

    @Test
    void 범위안에_황금사과가_있는지_알_수_있다() {
        Board boardWithGoldenApple = new Board(2, 2);

        assertThat(boardWithGoldenApple.hasGoldenAppleIn(List.of(
                new Coordinate(0, 0),
                new Coordinate(0, 1),
                new Coordinate(1, 0),
                new Coordinate(1, 1)
        ))).isTrue();
    }

    @Test
    void 범위안에_황금사과가_없는지_알_수_있다() {
        Board boardWithoutGoldenApple = TWO_BY_FOUR();

        assertThat(boardWithoutGoldenApple.hasGoldenAppleIn(List.of(
                new Coordinate(0, 0),
                new Coordinate(0, 1),
                new Coordinate(0, 2),
                new Coordinate(0, 3),
                new Coordinate(1, 0),
                new Coordinate(1, 1),
                new Coordinate(1, 2),
                new Coordinate(1, 3)
        ))).isFalse();
    }
}
