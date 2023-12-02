package com.snackgame.server.member.event;

import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.SocialMember;

public class GameSessionTransferEvent {

    private final Member victim;
    private final SocialMember socialMember;

    public GameSessionTransferEvent(Member victim, SocialMember socialMember) {
        this.victim = victim;
        this.socialMember = socialMember;
    }

    public Member getVictim() {
        return victim;
    }

    public SocialMember getSocialMember() {
        return socialMember;
    }
}
