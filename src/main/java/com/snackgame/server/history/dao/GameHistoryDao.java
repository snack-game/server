package com.snackgame.server.history.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.snackgame.server.history.controller.dto.GameHistoryResponse;

@Component
public class GameHistoryDao {

    private static final RowMapper<GameHistoryResponse> GAME_HISTORY_DTO_ROW_MAPPER = ((rs, rowNum) -> new GameHistoryResponse(
            rs.getLong("owner_id"),
            rs.getInt("score"),
            rs.getDate("updated_at").toLocalDate()
    ));
    private final JdbcTemplate jdbcTemplate;

    public GameHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GameHistoryResponse> selectByDate(Long memberId) {
        String sql = "SELECT owner_id, MAX(score) as score, DATE(updated_at) as updated_at " +
                     "FROM apple_game " +
                     "WHERE is_finished = true AND owner_id = ? AND DATEDIFF(dd, DATE(now()), DATE(updated_at)) <= 7 " +
                     "GROUP BY DATE(updated_at) " +
                     "ORDER BY updated_at DESC";

        return jdbcTemplate.query(sql, GAME_HISTORY_DTO_ROW_MAPPER, memberId);

    }

    public List<GameHistoryResponse> selectBySession(Long memberId, int size) {
        String sql = "SELECT owner_id, score, updated_at "
                     + "FROM apple_game "
                     + "WHERE is_finished = true and owner_id = ? "
                     + "ORDER BY updated_at desc "
                     + "limit ?";

        return jdbcTemplate.query(sql, GAME_HISTORY_DTO_ROW_MAPPER, memberId, size);
    }
}
