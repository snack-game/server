package com.snackgame.server.fixture;

import static com.snackgame.server.fixture.SeasonFixture.베타시즌;
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
        return new BestScore(10, 똥수().getId(), 1L, 베타시즌().getId());
    }

    public static BestScore 베타시즌_땡칠_10점() {
        return new BestScore(10, 땡칠().getId(), 2L, 베타시즌().getId());
    }

    public static BestScore 베타시즌_정환_8점() {
        return new BestScore(8, 정환().getId(), 3L, 베타시즌().getId());
    }

    public static BestScore 베타시즌_정언_8점() {
        return new BestScore(8, 정언().getId(), 4L, 베타시즌().getId());
    }

    public static BestScore 베타시즌_유진_6점() {
        return new BestScore(6, 유진().getId(), 5L, 베타시즌().getId());
    }

    public static void saveAll() {
        MemberFixture.saveAll();
        SeasonFixture.saveAll();
        FixtureSaver.save(
                베타시즌_똥수_10점(),
                베타시즌_땡칠_10점(),
                베타시즌_정환_8점(),
                베타시즌_정언_8점(),
                베타시즌_유진_6점()
        );
    }
}
