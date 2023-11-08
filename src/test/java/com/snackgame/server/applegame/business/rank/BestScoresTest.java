package com.snackgame.server.applegame.business.rank;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.땡칠2;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static com.snackgame.server.member.fixture.MemberFixture.시연;
import static com.snackgame.server.member.fixture.MemberFixture.주호;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import com.snackgame.server.member.fixture.MemberFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BestScoresTest {

    @Autowired
    private BestScores bestScores;

    @BeforeEach
    void setUp(@Autowired EntityManagerFactory entityManagerFactory) {
        MemberFixture.persistAllUsing(entityManagerFactory);
        bestScores.saveAll(List.of(
                new BestScore(10, 똥수().getId(), 1L),
                new BestScore(10, 땡칠().getId(), 2L),
                new BestScore(8, 땡칠2().getId(), 3L),
                new BestScore(8, 주호().getId(), 4L),
                new BestScore(6, 시연().getId(), 5L)
        ));
    }

    @Test
    void 높은_점수_순으로_50개의_랭킹을_찾아온다() {
        assertThat(bestScores.rank(50))
                .extracting("ownerName")
                .containsExactly(
                        똥수().getNameAsString(),
                        땡칠().getNameAsString(),
                        땡칠2().getNameAsString(),
                        주호().getNameAsString(),
                        시연().getNameAsString()
                );
    }

    @Test
    void 점수가_같으면_같은_순위로_가져온다() {
        var ranks = bestScores.rank(50);

        assertThat(ranks.get(0).getRank()).isEqualTo(ranks.get(1).getRank());
        assertThat(ranks.get(2).getRank()).isEqualTo(ranks.get(3).getRank());
    }

    @Test
    void 공동3등_2명_다음은_5등이다() {
        assertThat(bestScores.rank(50))
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
