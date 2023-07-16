package com.snackgame.server.applegame.business.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ApplesJsonConverterTest {

    private final ApplesJsonConverter converter = new ApplesJsonConverter();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
    }

    @Test
    void 사과들을_JSON으로_변환한다() throws JsonProcessingException {
        Board board = Board.ofRandomized(2, 2);
        String expectedJson = objectMapper.writeValueAsString(board.getApples());

        String json = converter.convertToDatabaseColumn(board.getApples());
        assertThat(json).isEqualTo(expectedJson);
    }

    @Test
    void JSON으로_표현된_사과들을_사과객체들로_변환한다() throws JsonProcessingException {
        Board board = Board.ofRandomized(2, 2);
        String json = objectMapper.writeValueAsString(board.getApples());

        List<List<Apple>> apples = converter.convertToEntityAttribute(json);
        assertThat(apples).isEqualTo(board.getApples());
    }
}
