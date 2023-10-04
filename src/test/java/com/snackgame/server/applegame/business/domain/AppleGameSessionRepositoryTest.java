package com.snackgame.server.applegame.business.domain;

import static com.snackgame.server.applegame.fixture.TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE;
import static com.snackgame.server.applegame.fixture.TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE_AS_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.TestTransaction;

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
        AppleGame game = new AppleGame(TWO_BY_TWO_WITH_GOLDEN_APPLE(), null);
        games.save(game);

        String applesJson = jdbcTemplate.queryForObject(
                "SELECT board FROM apple_game WHERE session_id = " + game.getSessionId(),
                String.class);
        assertThat(applesJson).isEqualTo(TWO_BY_TWO_WITH_GOLDEN_APPLE_AS_JSON);
    }

    @Test
    void 직렬화되어_저장된_게임판을_불러온다() {
        var saved = games.save(new AppleGame(TWO_BY_TWO_WITH_GOLDEN_APPLE(), null));
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        var game = games.findById(saved.getSessionId()).get();

        assertThat(game.getApples()).hasSize(2);
        assertThat(game.getApples().get(0)).hasSize(2);
        TestTransaction.end();
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
