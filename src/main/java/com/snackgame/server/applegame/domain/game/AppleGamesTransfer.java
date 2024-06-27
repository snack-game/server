package com.snackgame.server.applegame.domain.game;

import org.springframework.stereotype.Component;

import com.snackgame.server.member.service.AccountTransfer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppleGamesTransfer implements AccountTransfer {

    private final AppleGames appleGames;
    private final BestScoreTransfer bestScoreTransfer;

    @Override
    public void transferAll(Long victimMemberId, Long currentMemberId) {
        bestScoreTransfer.transfer(victimMemberId, currentMemberId);
        appleGames.transferAll(victimMemberId, currentMemberId);
    }
}
