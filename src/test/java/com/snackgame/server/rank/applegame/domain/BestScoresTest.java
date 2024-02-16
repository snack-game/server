package com.snackgame.server.rank.applegame.domain;

import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_땡칠_10점;
import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_땡칠_18점;
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
                        베타시즌_땡칠_18점().getOwnerId(),
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

        assertThat(ranks.get(4).getOwnerId()).isEqualTo(베타시즌_똥수_10점().getOwnerId());
        assertThat(ranks.get(4).getScore()).isEqualTo(베타시즌_똥수_10점().getScore());
        assertThat(ranks.get(5).getOwnerId()).isEqualTo(베타시즌_땡칠_10점().getOwnerId());
        assertThat(ranks.get(5).getScore()).isEqualTo(베타시즌_땡칠_10점().getScore());

        assertThat(ranks.get(4).getRank()).isEqualTo(ranks.get(5).getRank());
    }

    @Test
    void 공동1등_3명_다음은_4등이다() {
        assertThat(bestScores.rankLeaders(50))
                .extracting("rank", "score")
                .containsSubsequence(
                        tuple(1L, 20),
                        tuple(1L, 20),
                        tuple(1L, 20),
                        tuple(4L, 18)
                );
    }

    @Test
    void 사용자의_최고점수_랭킹을_가져온다() {
        assertThat(bestScores.rank(땡칠().getId()))
                .usingRecursiveComparison()
                .comparingOnlyFields("rank", "score", "ownerId")
                .isEqualTo(시즌1_땡칠_20점());
    }
}
