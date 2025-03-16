package com.snackgame.server.rank.history;

import static com.snackgame.server.rank.history.fixture.RankHistoryFixture.랭크_1등;
import static com.snackgame.server.rank.history.fixture.RankHistoryFixture.랭크_4등;
import static com.snackgame.server.rank.history.fixture.RankHistoryFixture.랭크_5등;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.rank.history.fixture.RankHistoryFixture;
import com.snackgame.server.support.general.DatabaseCleaningDataJpaTest;

@DatabaseCleaningDataJpaTest
public class RankHistoriesTest {

    @Autowired
    RankHistories rankHistories;

    @BeforeEach
    void setUp() {
        RankHistoryFixture.saveAll();
    }

    @DisplayName("랭크전적 조회 시 하위 3명을 조회한다")
    @Test
    void belowContainsThree() {

        List<RankHistoryWithName> belows = rankHistories.findBelowWithName(랭크_1등().getOwnerId(), 3);

        for (int i = 0; i < 3; i++) {
            System.out.println("id: " + belows.stream().map(RankHistoryWithName::getId).collect(Collectors.toList()));
        }

        assertThat(belows.stream().map(RankHistoryWithName::getId)).contains(랭크_4등().getOwnerId());

    }

    @DisplayName("랭크전적 조회 시 하위 4번째는 조회하지 않는다")
    @Test
    void belowDoesNotContainsFourth() {

        List<RankHistoryWithName> belows = rankHistories.findBelowWithName(랭크_1등().getOwnerId(), 3);

        assertThat(belows.stream().map(RankHistoryWithName::getId)).doesNotContain(랭크_5등().getOwnerId());

    }

}
