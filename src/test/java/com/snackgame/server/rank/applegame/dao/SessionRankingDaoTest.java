package com.snackgame.server.rank.applegame.dao;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.정환;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static com.snackgame.server.member.fixture.MemberFixture.유진;
import static com.snackgame.server.member.fixture.MemberFixture.정언;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.snackgame.server.applegame.domain.Coordinate;
import com.snackgame.server.applegame.domain.Range;
import com.snackgame.server.applegame.domain.game.AppleGame;
import com.snackgame.server.applegame.domain.game.AppleGames;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.member.fixture.MemberFixture;
import com.snackgame.server.rank.applegame.dao.dto.RankingDto;
import com.snackgame.server.support.general.DatabaseCleaningDataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DatabaseCleaningDataJpaTest
class SessionRankingDaoTest {

    private SessionRankingDao sessionRankingDao;

    private AppleGame first;
    private AppleGame second;
    private AppleGame third;
    private AppleGame fourth;
    private AppleGame fifth;

    @BeforeEach
    void setUp(@Autowired AppleGames appleGames, @Autowired JdbcTemplate jdbcTemplate) {
        MemberFixture.saveAll();

        this.sessionRankingDao = new SessionRankingDao(jdbcTemplate);

        this.first = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 똥수().getId()));
        first.removeApplesIn(new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        ));
        this.second = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId()));
        second.removeApplesIn(new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        ));
        this.third = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 정환().getId()));
        third.removeApplesIn(new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        ));
        this.fourth = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 유진().getId()));
        this.fifth = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 정언().getId()));

        first.finish();
        second.finish();
        third.finish();
        fourth.finish();
        fifth.finish();

        appleGames.flush();
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
        assertThat(sessionRankingDao.selectBestByScoreOf(third.getOwnerId()))
                .get().usingRecursiveComparison()
                .isEqualTo(new RankingDto(2, third.getSessionId()));
    }
}
