package com.snackgame.server.applegame.business;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.annotation.ServiceTest;
import com.snackgame.server.applegame.business.domain.apple.Apple;
import com.snackgame.server.applegame.business.domain.game.AppleGame;
import com.snackgame.server.applegame.business.domain.game.AppleGames;
import com.snackgame.server.applegame.controller.dto.CoordinateRequest;
import com.snackgame.server.applegame.controller.dto.RangeRequest;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.member.business.MemberService;
import com.snackgame.server.member.business.domain.Member;

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
        Member owner = memberService.createGuest();

        AppleGame game = appleGameService.startGameFor(owner);

        assertThat(game.getSessionId()).isNotNull();
        assertThat(game.getApples()).hasSize(AppleGame.DEFAULT_HEIGHT);
        assertThat(game.getApples().get(0)).hasSize(AppleGame.DEFAULT_WIDTH);
    }

    @Test
    void 게임을_조작한다() {
        Member owner = memberService.createGuest();
        AppleGame game = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), owner));
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

        appleGameService.placeMoves(owner, game.getSessionId(), rangeRequests);

        AppleGame found = appleGames.getBy(game.getSessionId());
        assertThat(found.getScore()).isEqualTo(6);
    }

    @Test
    void 황금사과를_제거하면_초기화된_판을_반환한다() {
        Member owner = memberService.createGuest();
        AppleGame game = appleGames.save(new AppleGame(TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE(), owner));

        List<RangeRequest> rangeRequests = List.of(new RangeRequest(
                new CoordinateRequest(0, 0),
                new CoordinateRequest(1, 0))
        );

        Optional<AppleGame> appleGame = appleGameService.placeMoves(owner, game.getSessionId(), rangeRequests);

        assertThat(appleGame).isPresent();
    }

    @Test
    void 황금사과를_제거하지_않으면_아무_판도_반환하지_않는다() {
        Member owner = memberService.createGuest();
        AppleGame game = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), owner));
        var rangeRequests = List.of(new RangeRequest(
                new CoordinateRequest(0, 0),
                new CoordinateRequest(1, 0))
        );

        Optional<AppleGame> appleGame = appleGameService.placeMoves(owner, game.getSessionId(), rangeRequests);

        assertThat(appleGame).isEmpty();
    }

    @Test
    void 게임판을_초기화한다() {
        Member owner = memberService.createGuest();
        AppleGame game = appleGameService.startGameFor(owner);
        List<List<Apple>> previousApples = game.getApples();
        LocalDateTime previousCreatedAt = game.getCreatedAt();

        appleGameService.restart(owner, game.getSessionId());

        assertThat(game.getApples()).isNotEqualTo(previousApples);
        assertThat(game.getCreatedAt()).isAfter(previousCreatedAt);
    }

    @Test
    void 게임을_끝낸다() {
        Member owner = memberService.createGuest();
        AppleGame game = appleGameService.startGameFor(owner);

        appleGameService.finish(owner, game.getSessionId());

        assertThat(game.isFinished()).isTrue();
    }
}
