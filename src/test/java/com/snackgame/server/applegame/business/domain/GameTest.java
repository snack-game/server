package com.snackgame.server.applegame.business.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.applegame.business.domain.exception.GameSessionExpiredException;
import com.snackgame.server.applegame.fixture.TestFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GameTest {

    @Test
    void 게임을_10_X_18사이즈의_게임판으로_생성한다() {
        var game = Game.ofRandomized();

        assertThat(game.getApples()).hasSize(10);
        assertThat(game.getApples()).allSatisfy(
                row -> assertThat(row).hasSize(18)
        );
    }

    @Test
    void 초기화한다() {
        var game = new Game(TestFixture.TWO_BY_FOUR());
        var range = new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        ));
        game.removeApplesIn(range);
        var previousApples = game.getApples();

        game.reset();

        assertThat(game.getApples()).isNotEqualTo(previousApples);
        assertThat(game.getScore()).isZero();
    }

    @Test
    void 특정_범위의_사과들을_제거하고_점수를_얻는다() {
        var game = new Game(TestFixture.TWO_BY_FOUR());
        var range = new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        ));

        game.removeApplesIn(range);

        assertThat(game.getScore()).isEqualTo(4);
    }

    @Test
    void 만든지_120초가_지나면_초기화할_수_없다() {
        var game = new Game(TestFixture.TWO_BY_FOUR(), LocalDateTime.now().minusSeconds(120));

        assertThatThrownBy(() -> game.reset())
                .isInstanceOf(GameSessionExpiredException.class)
                .hasMessage("게임 세션이 이미 종료되었습니다");
    }

    @Test
    void 만든지_120초가_지나면_제거할_수_없다() {
        var game = new Game(TestFixture.TWO_BY_FOUR(), LocalDateTime.now().minusSeconds(120));
        var range = new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        ));

        assertThatThrownBy(() -> game.removeApplesIn(range))
                .isInstanceOf(GameSessionExpiredException.class)
                .hasMessage("게임 세션이 이미 종료되었습니다");
    }
}
