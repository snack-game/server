package com.snackgame.server.applegame.business.domain;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.domain.SocialMember;
import com.snackgame.server.member.business.event.GameSessionTransferEvent;

@Component
public class GameSessionTransferListener {

    private final AppleGameSessionRepository appleGameSessions;

    public GameSessionTransferListener(AppleGameSessionRepository appleGameSessions) {
        this.appleGameSessions = appleGameSessions;
    }

    @EventListener
    public void transfer(GameSessionTransferEvent event) {
        Member victim = event.getVictim();
        SocialMember socialMember = event.getSocialMember();

        appleGameSessions.transferAll(victim, socialMember);
        victim.invalidate();
    }
}
