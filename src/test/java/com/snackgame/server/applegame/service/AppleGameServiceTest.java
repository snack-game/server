package com.snackgame.server.applegame.service;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.정환;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.applegame.controller.dto.CoordinateRequest;
import com.snackgame.server.applegame.controller.dto.RangeRequest;
import com.snackgame.server.applegame.domain.game.AppleGame;
import com.snackgame.server.applegame.domain.game.AppleGames;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.fixture.SeasonFixture;
import com.snackgame.server.member.fixture.MemberFixture;
import com.snackgame.server.support.general.ServiceTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class AppleGameServiceTest {

    @Autowired
    AppleGameService appleGameService;
    @Autowired
    AppleGames appleGames;

    @BeforeEach
    void setUp() {
        MemberFixture.saveAll();
        SeasonFixture.saveAll();
    }

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

        AppleGame found = appleGames.getBy(땡칠().getId(), game.getSessionId());
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
        AppleGame previous = appleGameService.startGameFor(땡칠().getId());

        appleGameService.restart(땡칠().getId(), previous.getSessionId());

        AppleGame current = appleGames.getBy(땡칠().getId(), previous.getSessionId());
        assertThat(current.getCreatedAt()).isAfter(previous.getCreatedAt());
        assertThat(current.getBoard())
                .usingRecursiveComparison()
                .isNotEqualTo(previous.getBoard());
    }

    @Test
    void 게임을_끝낸다() {
        Long sessionId = appleGameService.startGameFor(땡칠().getId()).getSessionId();
        appleGameService.finish(땡칠().getId(), sessionId);

        AppleGame game = appleGames.getBy(땡칠().getId(), sessionId);
        assertThat(game.isFinished()).isTrue();
    }

    @Test
    void 게임을_끝낼_때_점수와_백분율을_알_수_있다() {
        var game = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 정환().getId()));
        appleGameService.placeMoves(정환().getId(), game.getSessionId(), List.of(new RangeRequest(
                new CoordinateRequest(0, 1),
                new CoordinateRequest(1, 3)
        )));
        appleGameService.finish(정환().getId(), game.getSessionId());

        var otherGame = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId()));
        appleGameService.finish(땡칠().getId(), otherGame.getSessionId());

        var gameWithMidRangedScore = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId()));
        appleGameService.placeMoves(땡칠().getId(), gameWithMidRangedScore.getSessionId(), List.of(new RangeRequest(
                new CoordinateRequest(0, 0),
                new CoordinateRequest(1, 0)
        )));
        var result = appleGameService.finish(땡칠().getId(), gameWithMidRangedScore.getSessionId());

        assertThat(result.getScore()).isEqualTo(2);
        assertThat(result.getPercentile()).isEqualTo(50);
    }
}
