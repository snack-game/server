package com.snackgame.server.rank.history;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class RankHistoryTest {

    @DisplayName("순위가 높으면 전적을 갱신할 수 있다")
    @Test
    void compareRank() {
        RankHistory rankHistory = new RankHistory(땡칠().getId(), 15L, 10L, 1L);

        assertThat(rankHistory.canRenewBy(5L)).isTrue();
    }

}
