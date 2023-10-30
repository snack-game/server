package com.snackgame.server.applegame.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.business.domain.BoardConverter;
import com.snackgame.server.applegame.business.domain.Ranking;
import com.snackgame.server.member.business.domain.Group;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.domain.Name;

@Component
public class SessionRankingDao2 {

    private static final BoardConverter BOARD_CONVERTER = new BoardConverter();

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (rs, rowNum) -> new Member(
            rs.getLong("member_id"),
            new Name(rs.getString("member_name")),
            new Group()
    );

    private static final RowMapper<AppleGame> APPLE_GAME_ROW_MAPPER = (rs, rowNum) -> new AppleGame(
            rs.getLong("session_id"),
            MEMBER_ROW_MAPPER.mapRow(rs, rowNum),
            BOARD_CONVERTER.convertToEntityAttribute(rs.getString("board")),
            rs.getInt("score"),
            rs.getBoolean("is_ended")
    );

    private static final RowMapper<Ranking> RANKING_DTO_ROW_MAPPER = (rs, rowNum) -> new Ranking(
            rs.getInt("ranking"),
            APPLE_GAME_ROW_MAPPER.mapRow(rs, rowNum)
    );

    private static final String RANKING_VIEW = "SELECT rank() over (ORDER BY score DESC) as ranking, session_id " +
                                               "FROM apple_game " +
                                               "WHERE is_ended = true " +
                                               "ORDER BY score DESC ";

    private final JdbcTemplate jdbcTemplate;

    public SessionRankingDao2(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Ranking> selectTopsByScoreIn(int size) {
        String sql =
                "SELECT rank() over (ORDER BY score DESC) as ranking, m.id member_id, m.name member_name, a.session_id, a.board, a.score, a.is_ended "
                + "FROM apple_game a "
                + "LEFT JOIN member m on m.id = a.owner_id "
                + "WHERE is_ended = true "
                + "ORDER BY score DESC "
                + "limit ?";

        return jdbcTemplate.query(
                sql,
                RANKING_DTO_ROW_MAPPER,
                size
        );
    }

    public Optional<Ranking> selectBestByScoreOf(Long memberId) {
        String sql = "SELECT ranking, m.id member_id, m.name member_name, a.session_id, a.board, a.score, a.is_ended "
                     + "FROM apple_game a "
                     + "LEFT JOIN (" + RANKING_VIEW + ") whole_ranks ON a.session_id = whole_ranks.session_id "
                     + "LEFT JOIN member m on m.id = a.owner_id "
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
