package com.snackgame.server.member.business;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.member.business.domain.Group;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.domain.NameRandomizer;
import com.snackgame.server.member.business.exception.DuplicateNameException;
import com.snackgame.server.member.business.exception.MemberNotFoundException;
import com.snackgame.server.member.dao.MemberDao;
import com.snackgame.server.member.dao.dto.MemberDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;
    private final GroupService groupService;
    private final NameRandomizer nameRandomizer;

    @Transactional
    public Member createWith(String name, String groupName) {
        validateNoDuplicate(name);
        Group group = groupService.createIfNotExists(groupName);
        Member newMember = new Member(name, group);
        return save(newMember);
    }

    @Transactional
    public Member createWith(String name) {
        validateNoDuplicate(name);
        Member newMember = new Member(name);
        return save(newMember);
    }

    @Transactional
    public Member createGuest() {
        String name = nameRandomizer.get();
        while (doesExist(name)) {
            name = nameRandomizer.get();
        }
        Member guest = new Member(name);
        return save(guest);
    }

    @Transactional
    public void changeNameOf(Member member, String name) {
        validateNoDuplicate(name);
        member.changeNameTo(name);
        save(member);
    }

    @Transactional
    public void changeGroupNameOf(Member member, String groupName) {
        Group group = groupService.createIfNotExists(groupName);
        member.changeGroupTo(group);
        save(member);
    }

    @Transactional(readOnly = true)
    public Member findBy(Long id) {
        return memberDao.selectBy(id)
                .map(this::toMember)
                .orElseThrow(MemberNotFoundException::new);
    }



    private boolean doesExist(String name) {
        return memberDao.selectBy(name).isPresent();
    }

    private void validateNoDuplicate(String name) {
        if (memberDao.selectBy(name).isPresent()) {
            throw new DuplicateNameException();
        }
    }

    private Member save(Member member) {
        if (Objects.nonNull(member.getId())) {
            memberDao.update(MemberDto.of(member));
            return member;
        }
        return toMember(memberDao.insert(MemberDto.of(member)));
    }

    private Member toMember(MemberDto memberDto) {
        return new Member(
                memberDto.getId(),
                memberDto.getName(),
                groupService.findBy(memberDto.getGroupId())
        );
    }

    public List<String> findNamesStartWith(String prefix) {
        return memberDao.selectByNameLike(prefix).stream()
                .map(MemberDto::getName)
                .collect(Collectors.toList());
    }
}
