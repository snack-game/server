package com.snackgame.server.rank.history.fixture;


import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static com.snackgame.server.member.fixture.MemberFixture.유진;
import static com.snackgame.server.member.fixture.MemberFixture.정언;
import static com.snackgame.server.member.fixture.MemberFixture.정환;

import com.snackgame.server.member.fixture.MemberFixture;
import com.snackgame.server.rank.history.RankHistory;
import com.snackgame.server.support.fixture.FixtureSaver;

@SuppressWarnings("NonAsciiCharacters")
public class RankHistoryFixture {

    public static RankHistory 랭크_1등() {
        return new RankHistory(땡칠().getId(), 1L, 1L);
    }

    public static RankHistory 랭크_2등() {
        return new RankHistory(똥수().getId(), 2L, 2L);
    }

    public static RankHistory 랭크_3등() {
        return new RankHistory(정환().getId(), 3L, 3L);
    }

    public static RankHistory 랭크_4등() {
        return new RankHistory(유진().getId(), 4L, 4L);
    }

    public static RankHistory 랭크_5등() {
        return new RankHistory(정언().getId(), 5L, 5L);
    }

    public static void saveAll() {
        MemberFixture.saveAll();
        FixtureSaver.save(
                랭크_1등(),
                랭크_2등(),
                랭크_3등(),
                랭크_4등(),
                랭크_5등()
        );
    }
}
