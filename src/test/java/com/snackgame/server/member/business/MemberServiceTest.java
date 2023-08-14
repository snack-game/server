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
import com.snackgame.server.member.business.domain.MemberRepository;
import com.snackgame.server.member.business.domain.NameRandomizer;
import com.snackgame.server.member.business.exception.DuplicateNameException;
import com.snackgame.server.member.business.exception.MemberIdNotFoundException;
import com.snackgame.server.member.business.exception.MemberNotFoundException;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private MemberRepository memberRepository;

    NameRandomizer fakeNameRandomizer = new NameRandomizer() {
        private int index = 0;
        private final String[] names = {
                "게스트#abc",
                "게스트#abcd"
        };

        @Override
        public String get() {
            final int index = this.index++ % names.length;
            return names[index];
        }
    };

    @Test
    void 이름과_그룹이름으로_사용자를_생성한다() {
        Member created = memberService.createWith(똥수().getName(), 똥수().getGroup().getName());

        assertThat(memberRepository.findById(created.getId()))
                .get()
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(똥수());
    }

    @Test
    void 임시사용자를_생성한다() {
        Member guest = memberService.createGuest();

        var created = memberRepository.findById(guest.getId()).get();

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).startsWith("게스트");
    }

    @Test
    void 임시사용자_이름이_중복이면_다시_만든다() {
        memberService = new MemberService(memberRepository, groupService, fakeNameRandomizer);
        memberService.createWith("게스트#abc");

        Member guest = memberService.createGuest();

        var created = memberRepository.findById(guest.getId()).get();
        assertThat(created.getName()).isEqualTo("게스트#abcd");
    }

    @Test
    void 중복된_이름으로_생성할_수_없다() {
        memberService.createWith(똥수().getName());

        assertThatThrownBy(() -> memberService.createWith(똥수().getName(), null))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("이미 존재하는 이름입니다");
    }

    @Test
    void 사용자_이름을_수정한다() {
        Member member = memberService.createWith(똥수().getName());
        memberService.changeNameOf(member, "똥똥수");

        var found = memberRepository.findById(member.getId()).get();
        assertThat(found.getName())
                .isEqualTo("똥똥수");
    }

    @Test
    void 중복된_이름으로_수정할_수_없다() {
        memberService.createWith(땡칠().getName());
        Member member = memberService.createWith(똥수().getName());

        assertThatThrownBy(() -> memberService.changeNameOf(member, 땡칠().getName()))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("이미 존재하는 이름입니다");
    }

    @Test
    void 그룹_이름을_수정한다() {
        Member member = memberService.createWith(똥수().getName(), 똥수().getGroup().getName());
        memberService.changeGroupNameOf(member, "홍천고");

        assertThat(memberService.findBy(member.getId()).getGroup().getName())
                .isEqualTo("홍천고");
    }

    @Test
    void 사용자를_id로_찾는다() {
        Member created = memberService.createWith(땡칠().getName(), 땡칠().getGroup().getName());
        Member found = memberService.findBy(created.getId());
        assertThat(found)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt", "group.id")
                .isEqualTo(땡칠());
    }

    @Test
    void 사용자를_없는_id로_찾으면_예외를_던진다() {
        memberService.createWith(땡칠().getName());

        assertThatThrownBy(() -> memberService.findBy(999L))
                .isInstanceOf(MemberIdNotFoundException.class);
    }

    @Test
    void 사용자를_없는_이름으로_찾으면_예외를_던진다() {
        memberService.createWith(땡칠().getName());

        assertThatThrownBy(() -> memberService.findBy(똥수().getName()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 특정_이름으로_시작하는_사용자_이름들을_찾는다() {
        var fullName = memberService.createWith("땡칠이").getName();
        var shortName = memberService.createWith("땡칠").getName();

        assertThat(memberService.findNamesStartWith("땡칠"))
                .contains(fullName, shortName);
    }
}
