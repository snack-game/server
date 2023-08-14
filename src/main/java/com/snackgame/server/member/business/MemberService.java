package com.snackgame.server.member.business;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.member.business.domain.Group;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.domain.MemberRepository;
import com.snackgame.server.member.business.domain.NameRandomizer;
import com.snackgame.server.member.business.exception.DuplicateNameException;
import com.snackgame.server.member.business.exception.MemberIdNotFoundException;
import com.snackgame.server.member.business.exception.MemberNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository members;
    private final GroupService groupService;
    private final NameRandomizer nameRandomizer;

    @Transactional
    public Member createWith(String name, String groupName) {
        validateNoDuplicate(name);
        Group group = groupService.createIfNotExists(groupName);
        Member newMember = new Member(name, group);
        return members.save(newMember);
    }

    @Transactional
    public Member createWith(String name) {
        validateNoDuplicate(name);
        Member newMember = new Member(name);
        return members.save(newMember);
    }

    @Transactional
    public Member createGuest() {
        Member guest = new Member(generateDistinctName());
        return members.save(guest);
    }

    @Transactional
    public void changeNameOf(Member member, String name) {
        validateNoDuplicate(name);
        member.changeNameTo(name);
    }

    @Transactional
    public void changeGroupNameOf(Member member, String groupName) {
        Group group = groupService.createIfNotExists(groupName);
        member.changeGroupTo(group);
    }

    public Member findBy(Long id) {
        return members.findById(id)
                .orElseThrow(MemberIdNotFoundException::new);
    }

    public Member findBy(String name) {
        return members.findByName(name)
                .orElseThrow(MemberNotFoundException::new);
    }

    public List<String> findNamesStartWith(String prefix) {
        return members.findByNameStartingWith(prefix).stream()
                .map(Member::getName)
                .collect(Collectors.toList());
    }

    private String generateDistinctName() {
        String name = nameRandomizer.get();
        while (doesExist(name)) {
            name = nameRandomizer.get();
        }
        return name;
    }

    private boolean doesExist(String name) {
        return members.findByName(name).isPresent();
    }

    private void validateNoDuplicate(String name) {
        if (members.findByName(name).isPresent()) {
            throw new DuplicateNameException();
        }
    }
}
