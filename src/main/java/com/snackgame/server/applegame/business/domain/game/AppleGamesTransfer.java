package com.snackgame.server.applegame.business.domain.game;

import org.springframework.stereotype.Component;

import com.snackgame.server.member.business.domain.AccountTransfer;
import com.snackgame.server.member.business.domain.Member;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleGamesTransfer implements AccountTransfer {

    private final AppleGames appleGames;
    private final BestScoreTransfer bestScoreTransfer;

    @Override
    public void transferAll(Member victim, Member currentMember) {
        bestScoreTransfer.transfer(victim.getId(), currentMember.getId());
        appleGames.transferAll(victim, currentMember);
    }
}
