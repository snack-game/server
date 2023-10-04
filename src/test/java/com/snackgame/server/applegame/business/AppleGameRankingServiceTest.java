package com.snackgame.server.applegame.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.annotation.ServiceTest;
import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.business.domain.AppleGameSessionRepository;
import com.snackgame.server.applegame.business.domain.Coordinate;
import com.snackgame.server.applegame.business.domain.Range;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.member.business.MemberService;
import com.snackgame.server.member.business.domain.Member;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class AppleGameRankingServiceTest {

    @Autowired
    AppleGameService appleGameService;
    @Autowired
    MemberService memberService;
    @Autowired
    AppleGameSessionRepository appleGameSessions;

    @Autowired
    private AppleGameRankingService appleGameRankingService;

    @Test
    void 전체_랭킹을_가져온다() {
        Member owner = memberService.createGuest();
        AppleGame firstGame = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), owner));
        firstGame.removeApplesIn(new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        ));
        AppleGame secondGame = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), owner));
        secondGame.removeApplesIn(new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        ));
        AppleGame thirdGame = appleGameService.startGameOf(owner);
        firstGame.end();
        secondGame.end();
        thirdGame.end();
        appleGameSessions.flush();

        assertThat(appleGameRankingService.getEntireRankings())
                .extracting("ranking", "score")
                .containsExactly(
                        tuple(1, 4),
                        tuple(2, 2),
                        tuple(3, 0)
                );
    }

    @Test
    void 자신의_최대_랭킹을_가져온다() {
        Member otherMember = memberService.createGuest();
        AppleGame firstGame = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), otherMember));
        firstGame.removeApplesIn(new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        ));
        Member member = memberService.createGuest();
        AppleGame secondGame = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), member));
        secondGame.removeApplesIn(new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        ));
        AppleGame thirdGame = appleGameService.startGameOf(member);
        firstGame.end();
        secondGame.end();
        thirdGame.end();
        appleGameSessions.flush();

        assertThat(appleGameRankingService.getBestRankingOf(member.getId()))
                .extracting("score")
                .isEqualTo(2);
    }
}
