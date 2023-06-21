package com.snackgame.server.member.business;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.annotation.ServiceTest;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.exception.DuplicateNameException;
import com.snackgame.server.member.business.exception.MemberNotFoundException;
import com.snackgame.server.member.dao.MemberDao;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberDao memberDao;

    @Test
    void 이름과_그룹이름으로_사용자를_생성한다() {
        Member created = memberService.createWith(똥수().getName(), 똥수().getGroupName());

        assertThat(memberDao.selectBy(created.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(똥수());
    }

    @Test
    void 임시사용자를_생성한다() {
        Member guest = memberService.createGuest();

        var created = memberDao.selectBy(guest.getId()).get();

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).startsWith("게스트");
    }

    @Test
    void 중복된_이름으로_생성할_수_없다() {
        memberService.createWith(똥수().getName(), 똥수().getGroupName());

        assertThatThrownBy(() -> memberService.createWith(똥수().getName(), 똥수().getGroupName()))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("이미 존재하는 이름입니다");
    }

    @Test
    void 사용자_이름을_수정한다() {
        Member member = memberService.createWith(똥수().getName(), 똥수().getGroupName());
        memberService.changeNameOf(member, "똥똥수");

        Member found = memberDao.selectBy(member.getId()).get();
        assertThat(found.getName())
                .isEqualTo("똥똥수");
    }

    @Test
    void 중복된_이름으로_수정할_수_없다() {
        memberService.createWith(땡칠().getName(), 땡칠().getGroupName());
        Member member = memberService.createWith(똥수().getName(), 똥수().getGroupName());

        assertThatThrownBy(() -> memberService.changeNameOf(member, 땡칠().getName()))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("이미 존재하는 이름입니다");
    }

    @Test
    void 그룹_이름을_수정한다() {
        Member member = memberService.createWith(똥수().getName(), 똥수().getGroupName());
        memberService.changeGroupNameOf(member, "홍천고");

        Member found = memberDao.selectBy(member.getId()).get();
        assertThat(found.getGroupName())
                .isEqualTo("홍천고");
    }

    @Test
    void 사용자를_id로_찾는다() {
        Member created = memberService.createWith(땡칠().getName(), 땡칠().getGroupName());

        Member found = memberService.findBy(created.getId());
        assertThat(found)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(땡칠());
    }

    @Test
    void 사용자를_없는_id로_찾으면_예외를_던진다() {
        memberService.createWith(땡칠().getName(), 땡칠().getGroupName());

        assertThatThrownBy(() -> memberService.findBy(999L))
                .isInstanceOf(MemberNotFoundException.class);
    }
}
