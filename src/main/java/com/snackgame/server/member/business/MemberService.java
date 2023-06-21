package com.snackgame.server.member.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.domain.NameRandomizer;
import com.snackgame.server.member.business.exception.DuplicateNameException;
import com.snackgame.server.member.business.exception.MemberNotFoundException;
import com.snackgame.server.member.dao.MemberDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberDao memberDao;
    private final NameRandomizer nameRandomizer;

    @Transactional
    public Member createWith(String name, String groupName) {
        validateNoDuplicate(name);
        Member newMember = new Member(name, groupName);
        return memberDao.insert(newMember);
    }

    @Transactional
    public Member createGuest() {
        String name = nameRandomizer.get();
        while (doesExist(name)) {
            name = nameRandomizer.get();
        }
        return memberDao.insert(new Member(name));
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

    private boolean doesExist(String name) {
        return memberDao.selectBy(name).isPresent();
    }

    private void validateNoDuplicate(String name) {
        if (memberDao.selectBy(name).isPresent()) {
            throw new DuplicateNameException();
        }
    }
}
