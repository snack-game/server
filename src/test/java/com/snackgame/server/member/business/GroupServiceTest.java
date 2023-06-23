package com.snackgame.server.member.business;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.annotation.ServiceTest;
import com.snackgame.server.member.business.domain.Group;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class GroupServiceTest {

    @Autowired
    GroupService groupService;

    @Test
    void 없으면_만들어_반환한다() {
        Group created = groupService.createIfNotExists("숭실대학교");

        assertThat(created).isNotNull();
    }

    @Test
    void 있으면_찾아_반환한다() {
        Group created = groupService.createIfNotExists("숭실대학교");
        Group found = groupService.createIfNotExists("숭실대학교");

        assertThat(created)
                .usingRecursiveComparison()
                .isEqualTo(found);
    }

    @Test
    void id로_찾는다() {
        Group created = groupService.createIfNotExists("숭실대학교");

        Group found = groupService.findBy(created.getId());

        assertThat(created)
                .usingRecursiveComparison()
                .isEqualTo(found);
    }

    @Test
    void 특정_이름으로_시작하는_그룹이름들을_찾는다() {
        var fullName = groupService.createIfNotExists("숭실대학교").getName();
        var shortName = groupService.createIfNotExists("숭실대").getName();

        assertThat(groupService.findNamesStartWith("숭실대"))
                .contains(fullName, shortName);
    }
}
