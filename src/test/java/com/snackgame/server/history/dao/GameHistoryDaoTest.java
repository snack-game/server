package com.snackgame.server.history.dao;

import static com.snackgame.server.history.fixture.HistoryFixture.duplicateHistories;
import static com.snackgame.server.history.fixture.HistoryFixture.first;
import static com.snackgame.server.history.fixture.HistoryFixture.secondBest;
import static com.snackgame.server.history.fixture.HistoryFixture.sixthBest;
import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.snackgame.server.history.controller.dto.GameHistoryResponse;
import com.snackgame.server.history.fixture.HistoryFixture;
import com.snackgame.server.member.fixture.MemberFixture;
import com.snackgame.server.support.general.DatabaseCleaningDataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DatabaseCleaningDataJpaTest
class GameHistoryDaoTest {

    private GameHistoryDao gameHistoryDao;

    @BeforeEach
    void setUp(@Autowired JdbcTemplate jdbcTemplate) {
        MemberFixture.saveAll();
        HistoryFixture.saveAll();
        this.gameHistoryDao = new GameHistoryDao(jdbcTemplate);
    }

    @Test
    void 최근_25게임의_전적을_조회한다() {
        duplicateHistories(25);
        List<Integer> scores = gameHistoryDao.selectBySession(땡칠().getId(), 25)
                .stream()
                .map(GameHistoryResponse::getScore)
                .collect(Collectors.toList());
        assertThat(scores).containsOnly(800);
    }

    @Test
    void 최근_7일내의_전적들은_7개_이어야한다() {
        assertThat(gameHistoryDao.selectByDate(땡칠().getId())).hasSize(7);
    }

    @Test
    void 최근_7일내의_전적들은_최신날짜부터_조회한다() {
        List<Integer> scores = gameHistoryDao.selectByDate(땡칠().getId()).stream().
                map(GameHistoryResponse::getScore).
                collect(Collectors.toList());
        assertThat(scores).doesNotContain(first().getScore());
    }

    @Test
    void 최근_7일내의_전적들은_최고점수만_가져야한다() {
        List<Integer> scores = gameHistoryDao.selectByDate(땡칠().getId()).stream().
                map(GameHistoryResponse::getScore).
                collect(Collectors.toList());
        assertThat(scores).contains(secondBest().getScore(), sixthBest().getScore());
    }
}