package com.snackgame.server.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.member.dao.MemberDao;
import com.snackgame.server.member.domain.Guest;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.exception.DuplicateNameException;
import com.snackgame.server.member.exception.MemberNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;

    @Transactional
    public Member createWith(String name, String groupName) {
        validateNoDuplicate(name);
        Member newMember = new Member(name, groupName);
        return memberDao.insert(newMember);
    }

    @Transactional
    public Member createGuest() {
        Member guest = new Guest();

        return memberDao.insert(guest);
    }

    @Transactional
    public void changeNameOf(Member member, String name) {
        validateNoDuplicate(name);
        member.changeNameTo(name);
        memberDao.update(member);
    }

    @Transactional
    public void changeGroupNameOf(Member member, String groupName) {
        member.changeGroupNameTo(groupName);
        memberDao.update(member);
    }

    @Transactional(readOnly = true)
    public Member findBy(Long id) {
        return memberDao.selectBy(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void validateNoDuplicate(String name) {
        if (memberDao.selectBy(name).isPresent()) {
            throw new DuplicateNameException();
        }
    }
}
