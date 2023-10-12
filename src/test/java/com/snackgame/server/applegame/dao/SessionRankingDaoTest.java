package com.snackgame.server.applegame.dao;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.땡칠2;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static com.snackgame.server.member.fixture.MemberFixture.시연;
import static com.snackgame.server.member.fixture.MemberFixture.주호;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.business.domain.AppleGameSessionRepository;
import com.snackgame.server.applegame.business.domain.Coordinate;
import com.snackgame.server.applegame.business.domain.Range;
import com.snackgame.server.applegame.dao.dto.RankingDto;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.member.fixture.MemberFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SessionRankingDaoTest {

    private SessionRankingDao sessionRankingDao;

    private AppleGame first;
    private AppleGame second;
    private AppleGame third;
    private AppleGame fourth;
    private AppleGame fifth;

    @BeforeEach
    void setUp(
            @Autowired AppleGameSessionRepository appleGameSessions,
            @Autowired TestEntityManager entityManager,
            @Autowired JdbcTemplate jdbcTemplate
    ) {
        MemberFixture.persistAllWith(entityManager);

        this.sessionRankingDao = new SessionRankingDao(jdbcTemplate);

        this.first = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 똥수()));
        first.removeApplesIn(new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        ));
        this.second = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠()));
        second.removeApplesIn(new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        ));
        this.third = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠2()));
        third.removeApplesIn(new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        ));
        this.fourth = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 시연()));
        this.fifth = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 주호()));

        first.end();
        second.end();
        third.end();
        fourth.end();
        fifth.end();

        appleGameSessions.flush();
    }

    @Test
    void 높은_점수_순으로_50개의_랭킹을_찾아온다() {
        assertThat(sessionRankingDao.selectTopsByScoreIn(50))
                .extracting("sessionId")
                .containsExactly(
                        first.getSessionId(),
                        second.getSessionId(),
                        third.getSessionId(),
                        fourth.getSessionId(),
                        fifth.getSessionId()
                );
    }

    @Test
    void 점수가_같으면_같은_순위로_가져온다() {
        assertThat(sessionRankingDao.selectTopsByScoreIn(50))
                .extracting("ranking", "sessionId")
                .contains(
                        tuple(4, fourth.getSessionId()),
                        tuple(4, fifth.getSessionId())
                );
    }

    @Test
    void 공동2등_2명_다음은_4등이다() {
        assertThat(sessionRankingDao.selectTopsByScoreIn(50))
                .extracting("ranking", "sessionId")
                .contains(
                        tuple(2, second.getSessionId()),
                        tuple(2, third.getSessionId()),
                        tuple(4, fourth.getSessionId())
                );
    }

    @Test
    void 사용자의_최고점수_랭킹을_가져온다() {
        assertThat(sessionRankingDao.selectBestByScoreOf(third.getOwner().getId()))
                .get().usingRecursiveComparison()
                .isEqualTo(new RankingDto(2, third.getSessionId()));
    }
}
