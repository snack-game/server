package com.snackgame.server.applegame.business.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    BoardRepository boards;
    @Autowired
    JdbcTemplate jdbcTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
    }

    @Test
    void 사과들은_직렬화되어_저장된다() throws JsonProcessingException {
        Board board = Board.ofRandomized(4, 4);

        boards.save(board);

        String applesJson = jdbcTemplate.queryForObject("SELECT apples FROM BOARD WHERE id = " + board.getId(),
                String.class);
        String expectedJson = objectMapper.writeValueAsString(board.getApples());
        assertThat(applesJson).isEqualTo(expectedJson);
    }

    @Test
    void 직렬화되어_저장된_사과들을_불러온다() {
        Long fixtureBoardId = insertFixtureBoard();
        Board board = boards.findById(fixtureBoardId).get();

        assertThat(board.getApples()).hasSize(2);
        assertThat(board.getApples().get(0)).hasSize(2);
    }

    private Long insertFixtureBoard() {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("board")
                .usingGeneratedKeyColumns("id");

        return jdbcInsert.executeAndReturnKey(Map.of(
                "apples", "[[{\"number\":4},{\"number\":8}],[{\"number\":3},{\"number\":1}]]"
        )).longValue();
    }
}
