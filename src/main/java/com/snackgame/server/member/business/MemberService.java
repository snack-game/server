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
import com.snackgame.server.member.business.exception.MemberNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
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
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public List<String> findNamesStartWith(String prefix) {
        return memberRepository.findByNameStartingWith(prefix).stream()
                .map(Member::getName)
                .collect(Collectors.toList());
    }

    private boolean doesExist(String name) {
        return memberRepository.findByName(name).isPresent();
    }

    private void validateNoDuplicate(String name) {
        if (memberRepository.findByName(name).isPresent()) {
            throw new DuplicateNameException();
        }
    }

    private Member save(Member member) {
        return memberRepository.save(member);
    }
}
