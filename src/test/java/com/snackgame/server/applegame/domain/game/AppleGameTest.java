package com.snackgame.server.applegame.domain.game;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.applegame.domain.Coordinate;
import com.snackgame.server.applegame.domain.Range;
import com.snackgame.server.applegame.exception.AppleNotRemovableException;
import com.snackgame.server.applegame.exception.GameSessionExpiredException;
import com.snackgame.server.applegame.fixture.TestFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AppleGameTest {

    @Test
    void 게임을_10_X_12사이즈의_게임판으로_생성한다() {
        var game = AppleGame.ofRandomized(땡칠().getId());

        assertThat(game.getApples()).hasSize(10);
        assertThat(game.getApples()).allSatisfy(
                row -> assertThat(row).hasSize(12)
        );
    }

    @Test
    void 재시작한다() {
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId());
        var range = new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        );
        game.removeApplesIn(range);
        var previousBoard = game.getBoard();
        var previousCreatedAt = game.getCreatedAt();

        game.restart();

        assertThat(game.getBoard()).isNotEqualTo(previousBoard);
        assertThat(game.getScore()).isZero();
        assertThat(game.getCreatedAt()).isAfter(previousCreatedAt);
    }

    @Test
    void 특정_범위의_사과들을_제거하고_점수를_얻는다() {
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId());
        var range = new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        );

        game.removeApplesIn(range);

        assertThat(game.getScore()).isEqualTo(4);
    }

    @Test
    void 이미_제거된_위치의_사과들을_제거하면_예외가_발생한다() {
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId());
        game.removeApplesIn(new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        ));

        assertThatThrownBy(() -> game.removeApplesIn(new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        ))).isInstanceOf(AppleNotRemovableException.class);
    }

    @Test
    void 황금사과를_제거하면_판이_초기화된다() {
        var game = new AppleGame(TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE(), 땡칠().getId());
        game.removeApplesIn(new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 1)
        ));
        var previousApples = game.getApples();

        var golden = new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        );
        game.removeApplesIn(golden);

        assertThat(game.getApples()).isNotEqualTo(previousApples);
    }

    @Test
    void 황금사과를_제거해도_시작_시간은_변하지_않는다() {
        var game = new AppleGame(TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE(), 땡칠().getId());
        var range = new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        );
        var previousCreatedAt = game.getCreatedAt();

        game.removeApplesIn(range);

        assertThat(game.getCreatedAt()).isEqualTo(previousCreatedAt);
    }

    @Test
    void 황금사과를_제거해도_점수는_초기화되지_않는다() {
        var game = new AppleGame(TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE(), 땡칠().getId());
        var range = new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        );
        var expectedScore = game.getScore() + 2;

        game.removeApplesIn(range);

        assertThat(game.getScore()).isEqualTo(expectedScore);
    }

    @Test
    void 만든지_2분_그리고_여유시간이_지나면_초기화할_수_없다() {
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId(), LocalDateTime.now());

        assertThatThrownBy(() -> game.restart())
                .isInstanceOf(GameSessionExpiredException.class)
                .hasMessage("이미 종료된 게임입니다");
    }

    @Test
    void 만든지_2분_그리고_여유시간이_지나면_사과를_제거할_수_없다() {
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId(), LocalDateTime.now());
        var range = new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        );

        assertThatThrownBy(() -> game.removeApplesIn(range))
                .isInstanceOf(GameSessionExpiredException.class)
                .hasMessage("이미 종료된 게임입니다");
    }

    @Test
    void 게임을_끝낸다() {
        var game = AppleGame.ofRandomized(똥수().getId());

        game.finish();

        assertThat(game.isFinished()).isTrue();
    }
}
