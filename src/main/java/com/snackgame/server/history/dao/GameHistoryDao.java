package com.snackgame.server.history.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.snackgame.server.history.controller.dto.GameHistoryResponse;

@Component
public class GameHistoryDao {

    private static final RowMapper<GameHistoryResponse> GAME_HISTORY_DTO_ROW_MAPPER = ((rs, rowNum) -> new GameHistoryResponse(
            rs.getLong("session_id"),
            rs.getLong("owner_id"),
            rs.getInt("score"),
            rs.getDate("finished_at").toLocalDate()
    ));
    private final JdbcTemplate jdbcTemplate;

    public GameHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GameHistoryResponse> selectByDate(Long memberId) {
        String sql =
                "SELECT MAX(session_id) as session_id, owner_id, MAX(score) as score, DATE(finished_at) as finished_at "
                +
                "FROM apple_game " +
                "WHERE finished_at is not null AND owner_id = ? AND TIMESTAMPDIFF(DAY ,finished_at, now()) < 7 " +
                "GROUP BY DATE(finished_at) " +
                "ORDER BY finished_at DESC";

        return jdbcTemplate.query(sql, GAME_HISTORY_DTO_ROW_MAPPER, memberId);

    }

    public List<GameHistoryResponse> selectBySession(Long memberId, int size) {
        String sql = "SELECT session_id, owner_id, score, finished_at "
                     + "FROM apple_game "
                     + "WHERE finished_at is not null and owner_id = ? "
                     + "ORDER BY finished_at desc "
                     + "limit ?";

        return jdbcTemplate.query(sql, GAME_HISTORY_DTO_ROW_MAPPER, memberId, size);
    }
}
