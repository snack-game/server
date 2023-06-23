package com.snackgame.server.member.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import com.snackgame.server.member.business.domain.Group;

@Component
public class GroupDao {

    private static final RowMapper<Group> GROUP_ROW_MAPPER = (rs, rowNum) -> new Group(
            rs.getLong("id"),
            rs.getString("name")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public GroupDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member_group")
                .usingGeneratedKeyColumns("id");
    }

    public Group insert(Group group) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(group);

        Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Group(id, group.getName());
    }

    public Optional<Group> selectBy(Long id) {
        String sql = "SELECT * FROM member_group WHERE id = ?";

        List<Group> groups = jdbcTemplate.query(
                sql,
                GROUP_ROW_MAPPER,
                id
        );
        return groups.stream().findFirst();
    }

    public Optional<Group> selectBy(String name) {
        String sql = "SELECT * FROM member_group WHERE name = ?";

        List<Group> groups = jdbcTemplate.query(
                sql,
                GROUP_ROW_MAPPER,
                name
        );
        return groups.stream().findFirst();
    }

    public List<Group> selectByNameLike(String name) {
        String sql = "SELECT * FROM member_group WHERE name LIKE ?";

        return jdbcTemplate.query(
                sql,
                GROUP_ROW_MAPPER,
                name + "%"
        );
    }
}
