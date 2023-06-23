package com.snackgame.server.member.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.snackgame.server.member.dao.dto.MemberDto;

@Component
public class MemberDao {

    private static final RowMapper<MemberDto> MEMBER_DTO_ROW_MAPPER = (rs, rowNum) -> new MemberDto(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getObject("group_id", Long.class)
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public MemberDto insert(MemberDto member) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(member);

        Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new MemberDto(id, member.getName(), member.getGroupId());
    }

    public Optional<MemberDto> selectBy(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";

        List<MemberDto> members = jdbcTemplate.query(
                sql,
                MEMBER_DTO_ROW_MAPPER,
                id
        );
        return members.stream().findFirst();
    }

    public Optional<MemberDto> selectBy(String name) {
        String sql = "SELECT * FROM member WHERE name = ?";

        List<MemberDto> members = jdbcTemplate.query(
                sql,
                MEMBER_DTO_ROW_MAPPER,
                name
        );
        return members.stream().findFirst();
    }

    public void update(MemberDto member) {
        String sql = "UPDATE member SET name = ?, group_id = ? WHERE id = ?";

        jdbcTemplate.update(
                sql,
                member.getName(),
                member.getGroupId(),
                member.getId()
        );
    }

    public List<MemberDto> selectByNameLike(String name) {
        String sql = "SELECT * FROM member WHERE name LIKE ?";

        return jdbcTemplate.query(
                sql,
                MEMBER_DTO_ROW_MAPPER,
                name + "%"
        );
    }
}
