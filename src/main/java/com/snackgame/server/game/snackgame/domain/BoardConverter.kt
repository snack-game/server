package com.snackgame.server.game.snackgame.domain

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class BoardConverter : AttributeConverter<Board, String> {

    override fun convertToDatabaseColumn(board: Board): String {
        return try {
            OBJECT_MAPPER.writeValueAsString(board)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    override fun convertToEntityAttribute(dbJson: String): Board {
        return try {
            OBJECT_MAPPER.readValue(dbJson, Board::class.java)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private val OBJECT_MAPPER = ObjectMapper().apply {
            setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            setVisibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE)
        }
    }
}
