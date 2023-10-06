package com.snackgame.server.applegame.business.domain;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.applegame.business.exception.GameSessionExpiredException;
import com.snackgame.server.applegame.business.exception.NotOwnedException;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.member.business.domain.Member;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AppleGameTest {

    @Test
    void 게임을_10_X_12사이즈의_게임판으로_생성한다() {
        var game = AppleGame.ofRandomized(땡칠());

        assertThat(game.getApples()).hasSize(10);
        assertThat(game.getApples()).allSatisfy(
                row -> assertThat(row).hasSize(12)
        );
    }

    @Test
    void 초기화한다() {
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠());
        var range = new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        );
        game.removeApplesIn(range);
        var previousBoard = game.getBoard();
        var previousCreatedAt = game.getCreatedAt();

        game.reset();

        assertThat(game.getBoard()).isNotEqualTo(previousBoard);
        assertThat(game.getScore()).isZero();
        assertThat(game.getCreatedAt()).isAfter(previousCreatedAt);
    }

    @Test
    void 특정_범위의_사과들을_제거하고_점수를_얻는다() {
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠());
        var range = new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        );

        game.removeApplesIn(range);

        assertThat(game.getScore()).isEqualTo(4);
    }

    @Test
    void 황금사과를_제거하면_판이_초기화된다() {
        var game = new AppleGame(TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE(), 땡칠());
        var range = new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        );
        var appleAtRightBottom = game.getApples().get(1).get(1);

        game.removeApplesIn(range);

        assertThat(game.getApples().get(1).get(1)).isNotEqualTo(appleAtRightBottom);
    }

    @Test
    void 황금사과를_제거해도_시작_시간은_변하지_않는다() {
        var game = new AppleGame(TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE(), 땡칠());
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
        var game = new AppleGame(TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE(), 땡칠());
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
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠(), LocalDateTime.now().minusSeconds(125));

        assertThatThrownBy(() -> game.reset())
                .isInstanceOf(GameSessionExpiredException.class)
                .hasMessage("게임 세션이 이미 종료되었습니다");
    }

    @Test
    void 만든지_2분_그리고_여유시간이_지나면_사과를_제거할_수_없다() {
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠(), LocalDateTime.now().minusSeconds(125));
        var range = new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        );

        assertThatThrownBy(() -> game.removeApplesIn(range))
                .isInstanceOf(GameSessionExpiredException.class)
                .hasMessage("게임 세션이 이미 종료되었습니다");
    }

    @Test
    void 게임의_주인이_아니면_예외를_던진다() {
        Member owner = 똥수();
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), owner, LocalDateTime.now().minusSeconds(120));

        assertThatThrownBy(() -> game.validateOwnedBy(땡칠()))
                .isInstanceOf(NotOwnedException.class);
    }

    @Test
    void 게임을_끝낸다() {
        var game = AppleGame.ofRandomized(똥수());

        game.end();

        game.isDone();
    }
}
