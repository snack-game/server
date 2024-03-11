package com.snackgame.server.history.dao;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.domain.Coordinate;
import com.snackgame.server.applegame.domain.Range;
import com.snackgame.server.applegame.domain.game.AppleGame;
import com.snackgame.server.applegame.domain.game.AppleGames;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.member.fixture.MemberFixture;
import com.snackgame.server.support.general.ServiceTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class GameHistoryDaoTest {

    @Autowired
    AppleGames appleGames;
    private GameHistoryDao gameHistoryDao;
    private AppleGame first;
    private AppleGame second;
    private AppleGame third;
    private AppleGame fourth;
    private AppleGame fifth;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        MemberFixture.saveAll();

        this.gameHistoryDao = new GameHistoryDao(jdbcTemplate);

        this.first = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId(), LocalDateTime.now()));
        first.removeApplesIn(new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3)
        ));
        this.second = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId(), LocalDateTime.now()));
        second.removeApplesIn(new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        ));
        this.third = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId(), LocalDateTime.now()));
        third.removeApplesIn(new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0)
        ));
        this.fourth = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId(), LocalDateTime.now()));
        this.fifth = appleGames.save(
                new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId(), LocalDateTime.of(2024, 2, 5, 12, 13)));

        first.finish();
        second.finish();
        third.finish();
        fourth.finish();
        fifth.finish();

        appleGames.flush();

    }

    @Test
    @Transactional
    void 최근_25게임의_전적을_조회한다() {
        Assertions.assertThat(gameHistoryDao.selectBySession(땡칠().getId(), 25))
                .size().isEqualTo(5);
    }

    @Test
    @Transactional
    void 최근_7일간의_게임_전적을_조회한다() {
        Assertions.assertThat(gameHistoryDao.selectByDate(땡칠().getId()))
                .hasSize(1);
    }

}