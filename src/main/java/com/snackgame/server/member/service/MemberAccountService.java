package com.snackgame.server.member.service;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.common.file.ResourceResolver;
import com.snackgame.server.member.domain.Group;
import com.snackgame.server.member.domain.Guest;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.MemberRepository;
import com.snackgame.server.member.domain.Name;
import com.snackgame.server.member.domain.ProfileImage;
import com.snackgame.server.member.domain.SocialMember;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberAccountService {

    private final MemberRepository members;
    private final GroupService groupService;
    private final DistinctNaming distinctNaming;
    private final List<AccountIntegration> accountIntegrations;

    private final ResourceResolver resourceResolver;

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
        for (AccountIntegration integration : accountIntegrations) {
            integration.execute(victim.getId(), socialMember.getId());
        }
        victim.invalidate();
        return socialMember;
    }

    @Transactional
    public void changeNameOf(Long memberId, String name) {
        Member member = members.getById(memberId);
        Name otherName = new Name(name);
        distinctNaming.validate(otherName);
        member.changeNameTo(otherName);
    }

    @Transactional
    public void changeGroupNameOf(Long memberId, String groupName) {
        Member member = members.getById(memberId);
        Group group = groupService.createIfNotExists(groupName);
        member.changeGroupTo(group);
    }

    @Transactional
    public void changeProfileImageOf(Long memberId, Resource resource) {
        Member member = members.getById(memberId);
        URL resolved = resourceResolver.resolve(resource);
        member.changeProfileImageTo(new ProfileImage(resolved.toString()));
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
