package com.snackgame.server.rank.history;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.정언;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.fixture.BestScoreFixture;
import com.snackgame.server.game.metadata.Metadata;
import com.snackgame.server.game.session.event.SessionEndEvent;
import com.snackgame.server.rank.event.BestScoreRenewalEvent;
import com.snackgame.server.rank.history.fixture.RankHistoryFixture;
import com.snackgame.server.support.general.ServiceTest;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
public class RankHistoryRenewalTest {

    @Autowired
    RankHistories rankHistories;
    @Autowired
    RankHistoryRenewal rankHistoryRenewal;

    @BeforeEach
    void setUp() {
        RankHistoryFixture.saveAll();
        BestScoreFixture.saveAll();
    }

    @Transactional
    @DisplayName("랭크를 갱신하면 저장하고 다른 사람들의 랭크도 최신화 된다")
    @Test
    void record() {

        rankHistoryRenewal.renewHistoryWith(
                BestScoreRenewalEvent.of(new SessionEndEvent(Metadata.SNACK_GAME, 정언().getId(), 14, 100), 1L));

        assertThat(rankHistories.findByOwnerId(땡칠().getId()).getBeforeRank()).isEqualTo(2L);
    }

}
