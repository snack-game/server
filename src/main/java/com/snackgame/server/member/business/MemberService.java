package com.snackgame.server.member.business;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.member.business.domain.AccountTransfer;
import com.snackgame.server.member.business.domain.DistinctNaming;
import com.snackgame.server.member.business.domain.Group;
import com.snackgame.server.member.business.domain.Guest;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.domain.MemberRepository;
import com.snackgame.server.member.business.domain.Name;
import com.snackgame.server.member.business.domain.SocialMember;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository members;
    private final GroupService groupService;
    private final DistinctNaming distinctNaming;
    private final AccountTransfer accountTransfer;

    @Transactional
    public Member createWith(String name) {
        return createWith(name, null);
    }

    @Transactional
    public Member createWith(String name, String groupName) {
        Name newName = new Name(name);
        distinctNaming.validate(newName);

        Member newMember = new Member(newName);
        if (Objects.nonNull(groupName)) {
            newMember.changeGroupTo(groupService.createIfNotExists(groupName));
        }
        return members.save(newMember);
    }

    @Transactional
    public Member createGuest() {
        Guest guest = new Guest(distinctNaming.ofGuest());
        return members.save(guest);
    }

    @Transactional
    public Member integrate(Member victim, SocialMember socialMember) {
        accountTransfer.transferAll(victim, socialMember);
        return socialMember;
    }

    @Transactional
    public void changeNameOf(Member member, String name) {
        Name otherName = new Name(name);
        distinctNaming.validate(otherName);
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
}
