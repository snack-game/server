package com.snackgame.server.history.dao

import com.snackgame.server.history.controller.dto.GameHistoryResponse
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Component
import java.sql.ResultSet
import java.time.LocalDate

@Component
class SnackgameHistoryDao(
    private val jdbcTemplate: JdbcTemplate
) {

    fun selectByDate(
        memberId: Long,
        baseDate: LocalDate = LocalDate.now().minusDays(7 - 1)
    ): List<GameHistoryResponse> {
        val sql =
            """SELECT MAX(session_id) as session_id, owner_id, MAX(score) as score, DATE(expires_at) as expires_at 
                FROM snackgame 
                WHERE owner_id = ? AND ? <= date(expires_at) AND date(expires_at) <= now() 
                GROUP BY DATE(expires_at) ORDER BY expires_at DESC"""

        return jdbcTemplate.query(
            sql,
            GAME_HISTORY_DTO_ROW_MAPPER,
            memberId,
            baseDate
        )
    }

    fun selectBySession(memberId: Long, size: Int): List<GameHistoryResponse> {
        val sql = """SELECT session_id, owner_id, score, expires_at
                FROM snackgame
                WHERE owner_id = ? AND date(expires_at) <= now() 
                ORDER BY expires_at desc
                limit ?"""

        return jdbcTemplate.query(
            sql,
            GAME_HISTORY_DTO_ROW_MAPPER,
            memberId,
            size
        )
    }

    companion object {
        private val GAME_HISTORY_DTO_ROW_MAPPER = (RowMapper { rs: ResultSet, _: Int ->
            GameHistoryResponse(
                rs.getLong("session_id"),
                rs.getLong("owner_id"),
                rs.getInt("score"),
                rs.getDate("expires_at").toLocalDate()
            )
        })
    }
}
