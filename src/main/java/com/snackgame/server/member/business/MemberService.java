package com.snackgame.server.member.business;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.member.business.domain.Group;
import com.snackgame.server.member.business.domain.Guest;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.domain.MemberRepository;
import com.snackgame.server.member.business.domain.Name;
import com.snackgame.server.member.business.domain.NameRandomizer;
import com.snackgame.server.member.business.exception.DuplicateNameException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository members;
    private final GroupService groupService;
    private final NameRandomizer nameRandomizer;

    @Transactional
    public Member createWith(String name) {
        return createWith(name, null);
    }

    @Transactional
    public Member createWith(String name, String groupName) {
        Name newName = new Name(name);
        validateDuplicationOf(newName);

        Member newMember = new Member(newName);
        if (Objects.nonNull(groupName)) {
            newMember.changeGroupTo(groupService.createIfNotExists(groupName));
        }
        return members.save(newMember);
    }

    @Transactional
    public Member createGuest() {
        Guest guest = new Guest(generateDistinctName());
        return members.save(guest);
    }

    @Transactional
    public void changeNameOf(Member member, String name) {
        Name otherName = new Name(name);
        validateDuplicationOf(otherName);
        member.changeNameTo(otherName);
    }

    @Transactional
    public void changeGroupNameOf(Member member, String groupName) {
        Group group = groupService.createIfNotExists(groupName);
        member.changeGroupTo(group);
    }

    public Member getBy(Long id) {
        return members.getById(id);
    }

    public Member getBy(String name) {
        return members.getByName(new Name(name));
    }

    public List<String> findNamesStartWith(String prefix) {
        return members.findByNameStringStartingWith(prefix).stream()
                .map(Member::getNameAsString)
                .collect(Collectors.toList());
    }

    private Name generateDistinctName() {
        Name name = nameRandomizer.get();
        while (members.existsByName(name)) {
            name = nameRandomizer.get();
        }
        return name;
    }

    private void validateDuplicationOf(Name name) {
        if (members.existsByName(name)) {
            throw new DuplicateNameException();
        }
    }
}
