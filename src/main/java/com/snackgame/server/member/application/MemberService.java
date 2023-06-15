package com.snackgame.server.member.application;

import org.springframework.stereotype.Service;

import com.snackgame.server.member.dao.MemberDao;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.exception.DuplicateNameException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;

    public Member createWith(String name, String groupName) {
        validateNoDuplicate(name);
        Member newMember = new Member(name, groupName);
        return memberDao.insert(newMember);
    }

    public void changeNameOf(Member member, String name) {
        validateNoDuplicate(name);
        member.changeNameTo(name);
        memberDao.update(member);
    }

    public void changeGroupNameOf(Member member, String groupName) {
        member.changeGroupNameTo(groupName);
        memberDao.update(member);
    }

    private void validateNoDuplicate(String name) {
        if (memberDao.selectBy(name).isPresent()) {
            throw new DuplicateNameException();
        }
    }
}
