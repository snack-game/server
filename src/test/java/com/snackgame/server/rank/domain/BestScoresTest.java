package com.snackgame.server.rank.domain;

import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_땡칠_10점;
import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_똥수_10점;
import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_유진_6점;
import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_정언_8점;
import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_정환_8점;
import static com.snackgame.server.fixture.BestScoreFixture.시즌1_땡칠_20점;
import static com.snackgame.server.fixture.BestScoreFixture.시즌1_유진_20점;
import static com.snackgame.server.fixture.BestScoreFixture.시즌1_정언_8점;
import static com.snackgame.server.fixture.BestScoreFixture.시즌1_정환_20점;
import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
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
                .extracting("ownerId")
                .containsExactly(
                        시즌1_땡칠_20점().getOwnerId(),
                        시즌1_정환_20점().getOwnerId(),
                        시즌1_유진_20점().getOwnerId(),
                        베타시즌_똥수_10점().getOwnerId(),
                        베타시즌_땡칠_10점().getOwnerId(),
                        베타시즌_정환_8점().getOwnerId(),
                        베타시즌_정언_8점().getOwnerId(),
                        시즌1_정언_8점().getOwnerId(),
                        베타시즌_유진_6점().getOwnerId()
                );
    }

    @Test
    void 점수가_같으면_같은_순위로_가져온다() {
        var ranks = bestScores.rankLeaders(50);

        assertThat(ranks.get(3).getOwnerId()).isEqualTo(베타시즌_똥수_10점().getOwnerId());
        assertThat(ranks.get(3).getScore()).isEqualTo(베타시즌_똥수_10점().getScore());
        assertThat(ranks.get(4).getOwnerId()).isEqualTo(베타시즌_땡칠_10점().getOwnerId());
        assertThat(ranks.get(4).getScore()).isEqualTo(베타시즌_땡칠_10점().getScore());

        assertThat(ranks.get(3).getRank()).isEqualTo(ranks.get(4).getRank());
    }

    @Test
    void 공동1등_2명_다음은_3등이다() {
        assertThat(bestScores.rankLeaders(50))
                .extracting("rank", "score")
                .containsSubsequence(
                        tuple(1L, 20),
                        tuple(1L, 20),
                        tuple(1L, 20),
                        tuple(4L, 10)
                );
    }

    @Test
    void 사용자의_최고점수_랭킹을_가져온다() {
        var rank = bestScores.rank(땡칠().getId());

        assertThat(rank.getRank()).isEqualTo(1);
        assertThat(rank)
                .usingRecursiveComparison()
                .comparingOnlyFields("score", "ownerId")
                .isEqualTo(시즌1_땡칠_20점());
    }
}
