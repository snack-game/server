package com.snackgame.server.fixture;

import static com.snackgame.server.fixture.SeasonFixture.베타시즌;
import static com.snackgame.server.fixture.SeasonFixture.시즌1;
import static com.snackgame.server.game.metadata.Metadata.APPLE_GAME;
import static com.snackgame.server.game.metadata.Metadata.SNACK_GAME;
import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static com.snackgame.server.member.fixture.MemberFixture.유진;
import static com.snackgame.server.member.fixture.MemberFixture.정언;
import static com.snackgame.server.member.fixture.MemberFixture.정환;

import com.snackgame.server.member.fixture.MemberFixture;
import com.snackgame.server.rank.domain.BestScore;
import com.snackgame.server.support.fixture.FixtureSaver;

@SuppressWarnings("NonAsciiCharacters")
public class BestScoreFixture {

    public static BestScore 사과게임_베타시즌_똥수_10점() {
        return new BestScore(똥수().getId(), APPLE_GAME.getGameId(), 베타시즌().getId(), 1, 10, 1);
    }

    public static BestScore 사과게임_베타시즌_땡칠_10점() {
        return new BestScore(땡칠().getId(), APPLE_GAME.getGameId(), 베타시즌().getId(), 2, 10, 2);
    }

    public static BestScore 사과게임_베타시즌_정환_8점() {
        return new BestScore(정환().getId(), APPLE_GAME.getGameId(), 베타시즌().getId(), 3, 8, 3);
    }

    public static BestScore 사과게임_베타시즌_유진_6점() {
        return new BestScore(유진().getId(), APPLE_GAME.getGameId(), 베타시즌().getId(), 4, 6, 4);
    }

    public static BestScore 사과게임_베타시즌_정언_8점() {
        return new BestScore(정언().getId(), APPLE_GAME.getGameId(), 베타시즌().getId(), 5, 8, 5);
    }

    public static BestScore 사과게임_시즌1_땡칠_20점() {
        return new BestScore(땡칠().getId(), APPLE_GAME.getGameId(), 시즌1().getId(), 6, 20, 6);
    }

    public static BestScore 사과게임_시즌1_정환_20점() {
        return new BestScore(정환().getId(), APPLE_GAME.getGameId(), 시즌1().getId(), 7, 20, 7);
    }

    public static BestScore 사과게임_시즌1_유진_20점() {
        return new BestScore(유진().getId(), APPLE_GAME.getGameId(), 시즌1().getId(), 8, 20, 8);
    }

    public static BestScore 사과게임_시즌1_정언_8점() {
        return new BestScore(정언().getId(), APPLE_GAME.getGameId(), 시즌1().getId(), 9, 8, 9);
    }

    public static BestScore 스낵게임_시즌1_땡칠_20점() {
        return new BestScore(땡칠().getId(), SNACK_GAME.getGameId(), 시즌1().getId(), 11, 20, 10);
    }

    public static BestScore 스낵게임_시즌1_유진_20점() {
        return new BestScore(유진().getId(), SNACK_GAME.getGameId(), 시즌1().getId(), 10, 20, 11);
    }

    public static BestScore 스낵게임_시즌1_정언_8점() {
        return new BestScore(정언().getId(), SNACK_GAME.getGameId(), 시즌1().getId(), 13, 8, 13);
    }

    public static void saveAll() {
        MemberFixture.saveAll();
        SeasonFixture.saveAll();
        FixtureSaver.save(
                사과게임_베타시즌_똥수_10점(),
                사과게임_베타시즌_땡칠_10점(),
                사과게임_베타시즌_정환_8점(),
                사과게임_베타시즌_유진_6점(),
                사과게임_베타시즌_정언_8점(),
                사과게임_시즌1_땡칠_20점(),
                사과게임_시즌1_정환_20점(),
                사과게임_시즌1_유진_20점(),
                사과게임_시즌1_정언_8점(),
                스낵게임_시즌1_땡칠_20점(),
                스낵게임_시즌1_유진_20점(),
                스낵게임_시즌1_정언_8점()
        );
    }
}
