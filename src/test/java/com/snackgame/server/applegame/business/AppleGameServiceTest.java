package com.snackgame.server.applegame.business;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.annotation.ServiceTest;
import com.snackgame.server.applegame.business.domain.Apple;
import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.business.domain.AppleGameSessionRepository;
import com.snackgame.server.applegame.business.domain.Coordinate;
import com.snackgame.server.applegame.business.domain.Range;
import com.snackgame.server.applegame.controller.dto.MoveRequest;
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
    AppleGameSessionRepository appleGameSessions;

    @Test
    void 게임을_시작한다() {
        Member owner = memberService.createGuest();

        AppleGame game = appleGameService.startGameOf(owner);

        assertThat(game.getSessionId()).isNotNull();
        assertThat(game.getApples()).hasSize(AppleGame.DEFAULT_HEIGHT);
        assertThat(game.getApples().get(0)).hasSize(AppleGame.DEFAULT_WIDTH);
    }

    @Test
    void 게임을_조작한다() {
        Member owner = memberService.createGuest();
        AppleGame game = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), owner));
        List<MoveRequest> moves = List.of(
                new MoveRequest(0, 1),
                new MoveRequest(0, 3),
                new MoveRequest(1, 1),
                new MoveRequest(1, 3)
        );

        appleGameService.placeMoves(owner, game.getSessionId(), moves);

        AppleGame found = appleGameService.findBy(game.getSessionId());
        assertThat(found.getScore()).isEqualTo(4);
    }

    @Test
    void 게임판을_초기화한다() {
        Member owner = memberService.createGuest();
        AppleGame game = appleGameService.startGameOf(owner);
        List<List<Apple>> previousApples = game.getApples();
        LocalDateTime previousCreatedAt = game.getCreatedAt();

        appleGameService.resetBoard(owner, game.getSessionId());

        assertThat(game.getApples()).isNotEqualTo(previousApples);
        assertThat(game.getCreatedAt()).isAfter(previousCreatedAt);
    }

    @Test
    void 게임을_끝낸다() {
        Member owner = memberService.createGuest();
        AppleGame game = appleGameService.startGameOf(owner);

        appleGameService.endSession(owner, game.getSessionId());

        assertThat(game.isDone()).isTrue();
    }

    @Test
    void 특정_페이지의_끝난_게임들을_가져온다() {
        Member owner = memberService.createGuest();
        AppleGame firstGame = appleGameService.startGameOf(owner);
        AppleGame secondGame = appleGameService.startGameOf(owner);
        AppleGame runningGame = appleGameService.startGameOf(owner);

        firstGame.end();
        secondGame.end();

        assertThat(appleGameService.getEndedGamesAt(0, 10))
                .containsExactly(firstGame, secondGame)
                .doesNotContain(runningGame);
    }

    @Test
    void 특정_페이지의_끝난_게임들을_페이지_크기에_맞춰_가져온다() {
        Member owner = memberService.createGuest();
        AppleGame firstGame = appleGameService.startGameOf(owner);
        AppleGame secondGame = appleGameService.startGameOf(owner);
        AppleGame thirdGame = appleGameService.startGameOf(owner);
        AppleGame fourthGame = appleGameService.startGameOf(owner);

        firstGame.end();
        secondGame.end();
        thirdGame.end();
        fourthGame.end();

        assertThat(appleGameService.getEndedGamesAt(0, 3))
                .hasSize(3);
    }

    @Test
    void 특정_페이지의_끝난_게임들을_높은_점수부터_가져온다() {
        Member owner = memberService.createGuest();
        AppleGame firstGame = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), owner));
        firstGame.removeApplesIn(new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        )));
        AppleGame secondGame = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), owner));
        secondGame.removeApplesIn(new Range(List.of(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        )));
        AppleGame thirdGame = appleGameService.startGameOf(owner);

        firstGame.end();
        secondGame.end();
        thirdGame.end();

        assertThat(appleGameService.getEndedGamesAt(0, 10))
                .containsExactly(firstGame, secondGame, thirdGame);
    }
}
