package com.snackgame.server.applegame.business.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DataJpaTest
@EnableJpaAuditing
class AppleGameSessionRepositoryTest {

    @Autowired
    AppleGameSessionRepository games;
    @Autowired
    JdbcTemplate jdbcTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    EntityManager em;

    @BeforeEach
    void setUp() {
        objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
    }

    @Test
    void 게임판의_사과들은_직렬화되어_저장된다() throws JsonProcessingException {
        AppleGame game = AppleGame.ofRandomized(null);

        games.save(game);

        String applesJson = jdbcTemplate.queryForObject("SELECT apples FROM apple_game WHERE session_id = " + game.getSessionId(),
                String.class);
        String expectedJson = objectMapper.writeValueAsString(game.getApples());
        assertThat(applesJson).isEqualTo(expectedJson);
    }

    @Test
    void 직렬화되어_저장된_게임판을_불러온다() {
        Long fixtureGameId = insertFixtureGame();
        AppleGame game = games.findById(fixtureGameId).get();

        assertThat(game.getApples()).hasSize(2);
        assertThat(game.getApples().get(0)).hasSize(2);
    }

    private Long insertFixtureGame() {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("apple_game")
                .usingGeneratedKeyColumns("session_id");

        return jdbcInsert.executeAndReturnKey(Map.of(
                "score", 0,
                "apples", "[[{\"number\":4},{\"number\":8}],[{\"number\":3},{\"number\":1}]]",
                "is_ended", false
        )).longValue();
    }

    @Test
    void 사과를_저장하면_생성시각도_저장된다() {
        AppleGame game = AppleGame.ofRandomized(null);

        games.save(game);

        assertThat(game.getCreatedAt()).isNotNull();
    }

    @Test
    void 생성시각은_리셋시_덮어씌워진다() {
        AppleGame game = AppleGame.ofRandomized(null);
        game.reset();
        LocalDateTime localCreatedTime = game.getCreatedAt();

        games.save(game);

        assertThat(game.getUpdatedAt()).isAfter(localCreatedTime);
    }
}
