package com.snackgame.server.applegame.domain.game;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.snackgame.server.applegame.fixture.TestFixture;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AppleGameBoardConverterTest {

    private final AppleGameBoardConverter converter = new AppleGameBoardConverter();

    @Test
    void 게임판을_JSON으로_변환한다() {
        AppleGameBoard appleGameBoard = TestFixture.TWO_BY_FOUR();

        String serialized = converter.convertToDatabaseColumn(appleGameBoard);

        assertThat(serialized).isEqualTo(TestFixture.TWO_BY_FOUR_AS_JSON);
    }

    @Test
    void 황금사과를_포함한_게임판을_JSON으로_변환한다() {
        AppleGameBoard appleGameBoard = TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE();

        String serialized = converter.convertToDatabaseColumn(appleGameBoard);

        assertThat(serialized).isEqualTo(TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE_AS_JSON);
    }

    @Test
    void JSON_게임판을_사과객체들로_변환한다() {
        AppleGameBoard expected = TestFixture.TWO_BY_FOUR();

        var deserialized = converter.convertToEntityAttribute(TestFixture.TWO_BY_FOUR_AS_JSON);

        assertThat(deserialized)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    void 황금사과를_포함한_게임판을_역직렬화한다() {
        AppleGameBoard expected = TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE();

        var deserialized = converter.convertToEntityAttribute(TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE_AS_JSON);

        assertThat(deserialized)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
