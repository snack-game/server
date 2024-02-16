package com.snackgame.server.rank.applegame.domain;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static com.snackgame.server.member.fixture.MemberFixture.유진;
import static com.snackgame.server.member.fixture.MemberFixture.정언;
import static com.snackgame.server.member.fixture.MemberFixture.정환;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.fixture.BestScoreFixture;
import com.snackgame.server.support.general.DatabaseCleaningDataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DatabaseCleaningDataJpaTest
class BestScoresTest {

    @Autowired
    private BestScores bestScores;

    @BeforeEach
    void setUp() {
        BestScoreFixture.saveAll();
    }

    @Test
    void 높은_점수_순으로_50개의_랭킹을_찾아온다() {
        assertThat(bestScores.rankLeaders(50))
                .extracting("ownerName")
                .containsExactly(
                        똥수().getNameAsString(),
                        땡칠().getNameAsString(),
                        정환().getNameAsString(),
                        정언().getNameAsString(),
                        유진().getNameAsString()
                );
    }

    @Test
    void 점수가_같으면_같은_순위로_가져온다() {
        var ranks = bestScores.rankLeaders(50);

        assertThat(ranks.get(0).getRank()).isEqualTo(ranks.get(1).getRank());
        assertThat(ranks.get(2).getRank()).isEqualTo(ranks.get(3).getRank());
    }

    @Test
    void 공동3등_2명_다음은_5등이다() {
        assertThat(bestScores.rankLeaders(50))
                .extracting("rank", "score")
                .containsSubsequence(
                        tuple(3L, 8),
                        tuple(3L, 8),
                        tuple(5L, 6)
                );
    }

    @Test
    void 사용자의_최고점수_랭킹을_가져온다() {
        assertThat(bestScores.rank(땡칠().getId()))
                .extracting("rank", "score", "ownerName", "ownerGroupName")
                .containsExactly(1L, 10, 땡칠().getNameAsString(), 땡칠().getGroup().getName());
    }
}
