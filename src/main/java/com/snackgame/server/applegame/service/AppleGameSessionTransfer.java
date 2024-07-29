package com.snackgame.server.applegame.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.domain.game.AppleGames;
import com.snackgame.server.member.service.AccountIntegration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AppleGameSessionTransfer implements AccountIntegration {

    private final AppleGames appleGames;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void execute(Long victimMemberId, Long currentMemberId) {
        int transferredCount = appleGames.transferAll(victimMemberId, currentMemberId);

        log.debug(
                "memberId={}의 사과게임 세션 {}개를 memberId={}에게 이전했습니다",
                victimMemberId, transferredCount, currentMemberId
        );
    }
}
