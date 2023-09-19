package com.snackgame.server.applegame.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.snackgame.server.applegame.dao.dto.RankingDto;

@Component
public class SessionRankingDao {

    private static final RowMapper<RankingDto> RANKING_DTO_ROW_MAPPER = (rs, rowNum) -> new RankingDto(
            rs.getInt("ranking"),
            rs.getLong("session_id")
    );
    private static final String RANKING_VIEW = "SELECT rank() over (ORDER BY score DESC) as ranking, session_id " +
                                               "FROM apple_game " +
                                               "WHERE is_ended = true " +
                                               "ORDER BY score DESC ";

    private final JdbcTemplate jdbcTemplate;

    public SessionRankingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RankingDto> selectTopsByScoreIn(int size) {
        String sql = RANKING_VIEW
                     + "limit ?";

        return jdbcTemplate.query(
                sql,
                RANKING_DTO_ROW_MAPPER,
                size
        );
    }

    public Optional<RankingDto> selectBestByScoreOf(Long memberId) {
        String sql = "SELECT ranking, a.session_id "
                     + "FROM apple_game a "
                     + "LEFT JOIN (" + RANKING_VIEW + ") whole_ranks ON a.session_id = whole_ranks.session_id "
                     + "WHERE owner_id = ? and is_ended = true "
                     + "ORDER BY score DESC "
                     + "limit 1";

        return jdbcTemplate.query(
                sql,
                RANKING_DTO_ROW_MAPPER,
                memberId
        ).stream().findFirst();
    }
}
