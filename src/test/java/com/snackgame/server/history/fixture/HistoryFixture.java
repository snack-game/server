package com.snackgame.server.history.fixture;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;

import java.time.LocalDateTime;

import com.snackgame.server.applegame.domain.game.AppleGame;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.support.fixture.FixtureSaver;

@SuppressWarnings("NonAsciiCharacters")
public class HistoryFixture {

    public static AppleGame first() {
        return new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(7),
                100);
    }

    public static AppleGame second() {
        return new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(6),
                200);
    }

    public static AppleGame secondBest() {
        return new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(6),
                250);
    }

    public static AppleGame third() {
        return new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(5),
                300);
    }

    public static AppleGame fourth() {
        return new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(4),
                400);
    }

    public static AppleGame fifth() {
        return new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(3),
                500);
    }

    public static AppleGame sixth() {
        return new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(2),
                600);
    }

    public static AppleGame sixthBest() {
        return new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(2),
                650);
    }

    public static AppleGame seventh() {
        return new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(1),
                700);
    }

    public static AppleGame eighth() {
        return new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now(),
                800);
    }

    public static void duplicateHistories(int cnt) {

        for (int i = 0; i < cnt; i++) {
            AppleGame newGame = new AppleGame(
                    TestFixture.TWO_BY_FOUR(),
                    땡칠().getId(),
                    LocalDateTime.now(),
                    800);
            FixtureSaver.save(newGame);
        }
    }

    public static void saveAll() {
        FixtureSaver.save(
                first(),
                second(),
                secondBest(),
                third(),
                fourth(),
                fifth(),
                sixth(),
                sixthBest(),
                seventh(),
                eighth()
        );
    }
}
