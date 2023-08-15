package com.snackgame.server.applegame.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.business.domain.AppleGameSessionRepository;
import com.snackgame.server.applegame.business.domain.Coordinate;
import com.snackgame.server.applegame.business.domain.Range;
import com.snackgame.server.applegame.dao.dto.RankingDto;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.member.business.GroupService;
import com.snackgame.server.member.business.MemberService;
import com.snackgame.server.member.business.domain.AlphabetNameRandomizer;
import com.snackgame.server.member.business.domain.GroupRepository;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.domain.MemberRepository;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class SessionRankingDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SessionRankingDao sessionRankingDao;

    private Member owner;
    private AppleGame first;
    private AppleGame second;
    private AppleGame third;
    private AppleGame fourth;

    @BeforeEach
    void setUp(
            @Autowired MemberRepository members,
            @Autowired GroupRepository groups,
            @Autowired AppleGameSessionRepository appleGameSessions
    ) {
        this.sessionRankingDao = new SessionRankingDao(jdbcTemplate);

        this.owner = new MemberService(
                members,
                new GroupService(groups),
                new AlphabetNameRandomizer()
        ).createGuest();
        this.first = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), owner));
        first.removeApplesIn(new Range(List.of(
                new Coordinate(0, 1),
                new Coordinate(0, 3),
                new Coordinate(1, 1),
                new Coordinate(1, 3)
        )));
        this.second = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), owner));
        second.removeApplesIn(new Range(List.of(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        )));
        this.third = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), owner));
        this.fourth = appleGameSessions.save(new AppleGame(TestFixture.TWO_BY_FOUR(), owner));

        first.end();
        second.end();
        third.end();
        fourth.end();

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
                        fourth.getSessionId()
                );
    }

    @Test
    void 점수가_같으면_같은_순위로_가져온다() {
        assertThat(sessionRankingDao.selectTopsByScoreIn(50))
                .extracting("ranking", "sessionId")
                .contains(
                        tuple(3, third.getSessionId()),
                        tuple(3, fourth.getSessionId())
                );
    }

    @Test
    void 사용자의_최고점수_랭킹을_가져온다() {
        assertThat(sessionRankingDao.selectBestByScoreOf(owner.getId()))
                .get().usingRecursiveComparison()
                .isEqualTo(new RankingDto(1, first.getSessionId()));
    }
}
