package com.snackgame.server.applegame.domain.game;

import static com.snackgame.server.applegame.fixture.TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE;
import static com.snackgame.server.applegame.fixture.TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE_AS_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.TestTransaction;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snackgame.server.applegame.domain.apple.Apple;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.support.general.DatabaseCleaningDataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@EnableJpaAuditing
@DatabaseCleaningDataJpaTest
class AppleGamesTest {

    @Autowired
    AppleGames appleGames;
    @Autowired
    JdbcTemplate jdbcTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
    }

    @Test
    void 게임판의_사과들은_직렬화되어_저장된다() throws JsonProcessingException {
        AppleGame game = new AppleGame(TWO_BY_TWO_WITH_GOLDEN_APPLE(), null);
        appleGames.save(game);

        String applesJson = jdbcTemplate.queryForObject(
                "SELECT apple_game_board FROM apple_game WHERE session_id = " + game.getSessionId(),
                String.class);
        assertThat(applesJson).isEqualTo(TWO_BY_TWO_WITH_GOLDEN_APPLE_AS_JSON);
    }

    @Test
    void 직렬화되어_저장된_게임판을_불러온다() {
        var saved = appleGames.save(new AppleGame(TWO_BY_TWO_WITH_GOLDEN_APPLE(), null));
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        var game = appleGames.findById(saved.getSessionId()).get();

        assertThat(game.getApples()).hasSize(2);
        assertThat(game.getApples().get(0)).hasSize(2);
        TestTransaction.end();
    }

    @Test
    void 사과를_저장하면_생성시각도_저장된다() {
        AppleGame game = AppleGame.ofRandomized(null);

        appleGames.save(game);

        assertThat(game.getCreatedAt()).isNotNull();
    }

    @Test
    void 생성시각은_리셋시_덮어씌워진다() {
        AppleGame game = AppleGame.ofRandomized(null);
        game.restart();
        LocalDateTime localCreatedTime = game.getCreatedAt();

        appleGames.save(game);

        assertThat(game.getUpdatedAt()).isAfter(localCreatedTime);
    }

    @Test
    void 저장_후_다시_불러와도_존재하는지_구분이_가능한가() {
        AppleGame game = new AppleGame(TestFixture.TWO_BY_FOUR(), null);
        appleGames.save(game);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        var apples = appleGames.findById(game.getSessionId()).get().getApples();
        assertThat(apples.get(0).get(2).exists()).isFalse();
    }

    @Test
    void 저장_후_다시_불러와도_같은_사과인가() {
        AppleGame game = new AppleGame(TestFixture.TWO_BY_FOUR(), null);
        appleGames.save(game);
        Apple previous = game.getApples().get(0).get(2);
        TestTransaction.flagForCommit();
        TestTransaction.end();

        TestTransaction.start();
        var apples = appleGames.findById(game.getSessionId()).get().getApples();
        assertThat(apples.get(0).get(2)).isEqualTo(previous);
    }
}
