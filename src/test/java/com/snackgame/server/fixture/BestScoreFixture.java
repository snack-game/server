package com.snackgame.server.fixture;

import static com.snackgame.server.fixture.SeasonFixture.베타시즌;
import static com.snackgame.server.fixture.SeasonFixture.시즌1;
import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static com.snackgame.server.member.fixture.MemberFixture.유진;
import static com.snackgame.server.member.fixture.MemberFixture.정언;
import static com.snackgame.server.member.fixture.MemberFixture.정환;

import com.snackgame.server.member.fixture.MemberFixture;
import com.snackgame.server.rank.applegame.domain.BestScore;
import com.snackgame.server.support.fixture.FixtureSaver;

@SuppressWarnings("NonAsciiCharacters")
public class BestScoreFixture {

    public static BestScore 베타시즌_똥수_10점() {
        return new BestScore(10, 똥수().getId(), 베타시즌().getId(), 1L);
    }

    public static BestScore 베타시즌_땡칠_10점() {
        return new BestScore(10, 땡칠().getId(), 베타시즌().getId(), 2L);
    }

    public static BestScore 베타시즌_정환_8점() {
        return new BestScore(8, 정환().getId(), 베타시즌().getId(), 3L);
    }

    public static BestScore 베타시즌_유진_6점() {
        return new BestScore(6, 유진().getId(), 베타시즌().getId(), 4L);
    }

    public static BestScore 베타시즌_정언_8점() {
        return new BestScore(8, 정언().getId(), 베타시즌().getId(), 5L);
    }

    public static BestScore 시즌1_땡칠_20점() {
        return new BestScore(20, 땡칠().getId(), 시즌1().getId(), 6L);
    }

    public static BestScore 시즌1_정환_20점() {
        return new BestScore(20, 정환().getId(), 시즌1().getId(), 7L);
    }

    public static BestScore 시즌1_유진_20점() {
        return new BestScore(20, 유진().getId(), 시즌1().getId(), 8L);
    }

    public static BestScore 시즌1_정언_8점() {
        return new BestScore(8, 정언().getId(), 시즌1().getId(), 9L);
    }

    public static void saveAll() {
        MemberFixture.saveAll();
        SeasonFixture.saveAll();
        FixtureSaver.save(
                베타시즌_똥수_10점(),
                베타시즌_땡칠_10점(),
                베타시즌_정환_8점(),
                베타시즌_유진_6점(),
                베타시즌_정언_8점(),
                시즌1_땡칠_20점(),
                시즌1_정환_20점(),
                시즌1_유진_20점(),
                시즌1_정언_8점()
        );
    }
}
