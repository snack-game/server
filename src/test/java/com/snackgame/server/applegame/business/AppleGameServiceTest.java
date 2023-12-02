package com.snackgame.server.applegame.business;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.annotation.ServiceTest;
import com.snackgame.server.applegame.business.domain.game.AppleGame;
import com.snackgame.server.applegame.business.domain.game.AppleGames;
import com.snackgame.server.applegame.business.domain.game.Board;
import com.snackgame.server.applegame.controller.dto.CoordinateRequest;
import com.snackgame.server.applegame.controller.dto.RangeRequest;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.member.business.MemberService;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class AppleGameServiceTest {

    @Autowired
    AppleGameService appleGameService;
    @Autowired
    MemberService memberService;
    @Autowired
    AppleGames appleGames;

    @Test
    void 게임을_시작한다() {
        AppleGame game = appleGameService.startGameFor(땡칠().getId());

        assertThat(game.getSessionId()).isNotNull();
        assertThat(game.getApples()).hasSize(AppleGame.DEFAULT_HEIGHT);
        assertThat(game.getApples().get(0)).hasSize(AppleGame.DEFAULT_WIDTH);
    }

    @Test
    void 게임을_조작한다() {
        AppleGame game = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId()));
        List<RangeRequest> rangeRequests = List.of(
                new RangeRequest(
                        new CoordinateRequest(0, 1),
                        new CoordinateRequest(1, 3)
                ),
                new RangeRequest(
                        new CoordinateRequest(0, 0),
                        new CoordinateRequest(1, 0)
                )
        );

        appleGameService.placeMoves(땡칠().getId(), game.getSessionId(), rangeRequests);

        AppleGame found = appleGames.getBy(game.getSessionId());
        assertThat(found.getScore()).isEqualTo(6);
    }

    @Test
    void 황금사과를_제거하면_초기화된_판을_반환한다() {
        AppleGame game = appleGames.save(new AppleGame(TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE(), 땡칠().getId()));
        List<RangeRequest> rangeRequests = List.of(new RangeRequest(
                new CoordinateRequest(0, 0),
                new CoordinateRequest(1, 0))
        );

        Optional<AppleGame> appleGame = appleGameService.placeMoves(땡칠().getId(), game.getSessionId(), rangeRequests);

        assertThat(appleGame).isPresent();
    }

    @Test
    void 황금사과를_제거하지_않으면_판을_반환하지_않는다() {
        AppleGame game = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId()));
        var rangeRequests = List.of(new RangeRequest(
                new CoordinateRequest(0, 0),
                new CoordinateRequest(1, 0))
        );

        Optional<AppleGame> appleGame = appleGameService.placeMoves(땡칠().getId(), game.getSessionId(), rangeRequests);

        assertThat(appleGame).isEmpty();
    }

    @Test
    void 게임을_재시작한다() {
        AppleGame game = appleGameService.startGameFor(땡칠().getId());
        Board previousBoard = game.getBoard();
        LocalDateTime previousCreatedAt = game.getCreatedAt();

        appleGameService.restart(땡칠().getId(), game.getSessionId());

        assertThat(game.getBoard()).isNotEqualTo(previousBoard);
        assertThat(game.getCreatedAt()).isAfter(previousCreatedAt);
    }

    @Test
    void 게임을_끝낸다() {
        AppleGame game = appleGameService.startGameFor(땡칠().getId());

        appleGameService.finish(땡칠().getId(), game.getSessionId());

        assertThat(game.isFinished()).isTrue();
    }
}
