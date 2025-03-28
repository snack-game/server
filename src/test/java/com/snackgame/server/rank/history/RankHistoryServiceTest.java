package com.snackgame.server.rank.history;

import static com.snackgame.server.rank.history.fixture.RankHistoryFixture.랭크_1등;
import static com.snackgame.server.rank.history.fixture.RankHistoryFixture.랭크_2등;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.rank.history.fixture.RankHistoryFixture;
import com.snackgame.server.support.general.ServiceTest;

@ServiceTest
public class RankHistoryServiceTest {

    private final static Integer SURPASS_MEMBER_NUM = 3;
    private final static Integer MAX_MEMBER_SIZE = 5;
    @Autowired
    private RankHistoryService service;

    @BeforeEach
    void setUp() {
        RankHistoryFixture.saveAll();
    }

    @Test
    void differenceLessThan5() {
        List<RankHistoryWithName> result = service.findMemberBelow(랭크_1등().getOwnerId());
        int dif = 랭크_1등().getBeforeRank().intValue() - 랭크_1등().getCurrentRank().intValue();
        assertThat(result).hasSize(dif);
    }

    @Test
    void differenceMoreThan5() {
        List<RankHistoryWithName> results = service.findMemberBelow(랭크_2등().getOwnerId());
        assertThat(results).hasSizeBetween(SURPASS_MEMBER_NUM, MAX_MEMBER_SIZE);
    }
}
