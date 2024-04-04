package com.snackgame.server.history.dao;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.domain.game.AppleGame;
import com.snackgame.server.applegame.domain.game.AppleGames;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.history.controller.dto.GameHistoryResponse;
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
    private AppleGame secondBest;
    private AppleGame third;
    private AppleGame fourth;
    private AppleGame fifth;
    private AppleGame sixth;
    private AppleGame sixthBest;
    private AppleGame seventh;
    private AppleGame eighth;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        MemberFixture.saveAll();

        this.gameHistoryDao = new GameHistoryDao(jdbcTemplate);

        this.first = appleGames.save(new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(7),
                100));

        this.second = appleGames.save(new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(6),
                200));

        this.secondBest = appleGames.save(new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(6),
                250));

        this.third = appleGames.save(new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(5),
                300));

        this.fourth = appleGames.save(new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(4),
                400));

        this.fifth = appleGames.save(new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(3),
                500));

        this.sixth = appleGames.save(new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(2),
                600));

        this.sixthBest = appleGames.save(new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(2),
                650));

        this.seventh = appleGames.save(new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now().minusDays(1),
                700));

        this.eighth = appleGames.save(new AppleGame(
                TestFixture.TWO_BY_FOUR(),
                땡칠().getId(),
                LocalDateTime.now(),
                800));

        appleGames.flush();

    }

    @Test
    @Transactional
    void 최근_25게임의_전적을_조회한다() {
        assertThat(gameHistoryDao.selectBySession(땡칠().getId(), 25)).size().isEqualTo(10);
    }

    @Test
    @Transactional
    void 최근_7일내의_전적들은_7개_이어야한다() {
        assertThat(gameHistoryDao.selectByDate(땡칠().getId())).hasSize(7);
    }

    @Test
    @Transactional
    void 최근_7일내의_전적들은_날짜_내림차순이어야한다() {
        List<Long> sessionIds = gameHistoryDao.selectByDate(땡칠().getId()).stream().
                map(GameHistoryResponse::getSession_id).
                collect(Collectors.toList());
        assertThat(sessionIds).doesNotContain(first.getSessionId());
    }

    @Test
    @Transactional
    void 최근_7일내의_전적들은_최고점수만_가져야한다() {
        List<Long> sessionIds = gameHistoryDao.selectByDate(땡칠().getId()).stream().
                map(GameHistoryResponse::getSession_id).
                collect(Collectors.toList());
        assertThat(sessionIds).contains(secondBest.getSessionId(), sixthBest.getSessionId());
    }
}