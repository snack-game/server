package com.snackgame.server.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.StatusService;
import com.snackgame.server.support.general.ServiceTest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class StatusServiceTest {

    @Autowired
    private StatusService statusService;

    @Autowired
    private MemberService memberService;

    @Test
    void 레벨업을_테스트한다() {
        //given
        Member member = memberService.createWith("박정환");

        //when
        statusService.updateStatus(member.getStatus(), 1080);

        //then
        Assertions.assertThat(member.getStatus().getLevel()).isEqualTo(4);
    }

}