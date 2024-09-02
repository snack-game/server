package com.snackgame.server.applegame.domain.game;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter(autoApply = true)
public class AppleGameBoardConverter implements AttributeConverter<AppleGameBoard, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
    }

    @Override
    public String convertToDatabaseColumn(AppleGameBoard appleGameBoard) {
        try {
            return OBJECT_MAPPER.writeValueAsString(appleGameBoard);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AppleGameBoard convertToEntityAttribute(String dbJson) {
        try {
            return OBJECT_MAPPER.readValue(dbJson, AppleGameBoard.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
