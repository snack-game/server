package com.snackgame.server.member;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.member.domain.Guest;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.MemberRepository;
import com.snackgame.server.member.domain.Name;
import com.snackgame.server.member.domain.Status;
import com.snackgame.server.member.exception.DuplicateNameException;
import com.snackgame.server.member.exception.MemberNotFoundException;
import com.snackgame.server.support.general.ServiceTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class MemberAccountServiceTest {

    @Autowired
    private MemberAccountService memberAccountService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 이름과_그룹이름으로_사용자를_생성한다() {
        Member created = memberAccountService.createWith(똥수().getNameAsString(), 똥수().getGroup().getName());

        assertThat(memberRepository.getById(created.getId()))
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "name", "group.name")
                .isEqualTo(똥수());
    }

    @Test
    void 그룹은_null로_하고_이름만으로_사용자를_생성한다() {
        Member expected = 똥수();
        expected.changeGroupTo(null);
        Member created = memberAccountService.createWith(똥수().getNameAsString(), null);

        assertThat(memberRepository.getById(created.getId()))
                .usingRecursiveComparison()
                .comparingOnlyFields("id", "name", "group.name")
                .isEqualTo(expected);
    }

    @Test
    void 임시사용자를_생성한다() {
        Member guest = memberAccountService.createGuest();

        var created = memberRepository.findById(guest.getId()).get();

        assertThat(created.getId()).isNotNull();
        assertThat(created.getNameAsString()).startsWith("게스트");
    }

    @Test
    void 임시사용자_이름이_중복이면_다시_만든다() {
        Guest existing = memberRepository.save(new Guest(new Name("게스트#abc")));

        Member guest = memberAccountService.createGuest();

        assertThat(guest.getNameAsString()).isNotEqualTo(existing.getNameAsString());
    }

    @Test
    void 중복된_이름으로_생성할_수_없다() {
        memberAccountService.createWith(똥수().getNameAsString());

        assertThatThrownBy(() -> memberAccountService.createWith(똥수().getNameAsString(), null))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("이미 존재하는 이름입니다");
    }

    @Test
    void 사용자_이름을_수정한다() {
        Member member = memberAccountService.createWith(똥수().getNameAsString());
        memberAccountService.changeNameOf(member.getId(), "똥똥수");

        var found = memberRepository.findById(member.getId()).get();
        assertThat(found.getNameAsString())
                .isEqualTo("똥똥수");
    }

    @Test
    void 중복된_이름으로_수정할_수_없다() {
        memberAccountService.createWith(땡칠().getNameAsString());
        Member member = memberAccountService.createWith(똥수().getNameAsString());

        assertThatThrownBy(() -> memberAccountService.changeNameOf(member.getId(), 땡칠().getNameAsString()))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("이미 존재하는 이름입니다");
    }

    @Test
    void 그룹_이름을_수정한다() {
        Member member = memberAccountService.createWith(똥수().getNameAsString(), 똥수().getGroup().getName());
        memberAccountService.changeGroupNameOf(member.getId(), "홍천고");

        assertThat(memberAccountService.getBy(member.getId()).getGroup().getName())
                .isEqualTo("홍천고");
    }

    @Test
    void 사용자를_id로_찾는다() {
        Member created = memberAccountService.createWith(땡칠().getNameAsString(), 땡칠().getGroup().getName());
        Member found = memberAccountService.getBy(created.getId());
        assertThat(found)
                .usingRecursiveComparison()
                .comparingOnlyFields("name", "group.name")
                .isEqualTo(땡칠());
    }

    @Test
    void 사용자를_없는_id로_찾으면_예외를_던진다() {
        memberAccountService.createWith(땡칠().getNameAsString());

        assertThatThrownBy(() -> memberAccountService.getBy(999L))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 사용자를_없는_이름으로_찾으면_예외를_던진다() {
        memberAccountService.createWith(땡칠().getNameAsString());

        assertThatThrownBy(() -> memberAccountService.getBy(똥수().getNameAsString()))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void 특정_이름으로_시작하는_사용자_이름들을_찾는다() {
        var fullName = memberAccountService.createWith("땡칠이").getNameAsString();
        var shortName = memberAccountService.createWith("땡칠").getNameAsString();

        assertThat(memberAccountService.findNamesStartWith("땡칠"))
                .contains(fullName, shortName);
    }
}
