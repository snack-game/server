package com.snackgame.server.member.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.snackgame.server.member.domain.Member;

@Component
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    public Member insert(Member member) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(member);

        long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Member(
                id,
                member.getName(),
                member.getGroupName()
        );
    }

    public Optional<Member> selectBy(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";

        List<Member> members = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Member(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("group_name")
                ),
                id
        );
        return members.stream().findFirst();
    }

    public Optional<Member> selectBy(String name) {
        String sql = "SELECT * FROM member WHERE name = ?";

        List<Member> members = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Member(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("group_name")
                ),
                name
        );
        return members.stream().findFirst();
    }

    public void update(Member member) {
        String sql = "UPDATE member SET name = ?, group_name = ? WHERE id = ?";

        jdbcTemplate.update(sql, member.getName(), member.getGroupName(), member.getId());
    }
}
