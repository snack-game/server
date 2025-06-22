package com.snackgame.server.game.snackgame.core.domain


import com.snackgame.server.game.snackgame.fixture.BoardFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BoardConverterTest {

    private val converter = BoardConverter()

    @Test
    fun `게임판을 Json으로 저장할 수 있다`() {
        val board = BoardFixture.TWO_BY_FOUR()

        val converted = converter.convertToDatabaseColumn(board)

        assertThat(converted).isEqualTo(BoardFixture.TWO_BY_FOUR_AS_JSON)
    }

    @Test
    fun `Json을 스낵으로 변환할 수 있다`() {
        val board = BoardFixture.TWO_BY_FOUR()

        var deserialized = converter.convertToEntityAttribute(BoardFixture.TWO_BY_FOUR_AS_JSON)

        assertThat(deserialized).usingRecursiveComparison().isEqualTo(board)
    }
}
