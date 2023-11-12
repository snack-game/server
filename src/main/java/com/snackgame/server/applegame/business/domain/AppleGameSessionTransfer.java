package com.snackgame.server.applegame.business.domain;

import org.springframework.stereotype.Component;

import com.snackgame.server.member.business.domain.AccountTransfer;
import com.snackgame.server.member.business.domain.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleGameSessionTransfer implements AccountTransfer {

    private final AppleGameSessionRepository appleGameSessions;
    private final BestScoreTransfer bestScoreTransfer;

    @Override
    public void transferAll(Member victim, Member newMember) {
        appleGameSessions.transferAll(victim, newMember);
        bestScoreTransfer.transfer(victim.getId(), newMember.getId());
        victim.invalidate();
    }
}
