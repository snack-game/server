package com.snackgame.server.applegame.business;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.business.domain.game.AppleGame;
import com.snackgame.server.applegame.business.domain.game.AppleGames;
import com.snackgame.server.applegame.business.domain.game.Board;
import com.snackgame.server.applegame.business.event.GameEndEvent;
import com.snackgame.server.applegame.controller.dto.RangeRequest;
import com.snackgame.server.member.business.domain.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AppleGameService {

    private final AppleGames appleGames;
    private final ApplicationEventPublisher eventPublisher;

    public AppleGame startGameFor(Member member) {
        AppleGame game = AppleGame.ofRandomized(member);
        return appleGames.save(game);
    }

    public Optional<AppleGame> placeMoves(Member member, Long sessionId, List<RangeRequest> rangeRequests) {
        AppleGame game = appleGames.getBy(sessionId);
        game.validateOwnedBy(member);

        Board previous = game.getBoard();
        rangeRequests.forEach(request -> game.removeApplesIn(request.toRange()));

        if (!game.getBoard().equals(previous)) {
            return Optional.of(game);
        }
        return Optional.empty();
    }

    public AppleGame restart(Member member, Long sessionId) {
        AppleGame game = appleGames.getBy(sessionId);
        game.validateOwnedBy(member);
        game.restart();
        return game;
    }

    public void finish(Member member, Long sessionId) {
        AppleGame game = appleGames.getBy(sessionId);
        game.validateOwnedBy(member);
        game.finish();
        eventPublisher.publishEvent(new GameEndEvent(game));
    }
}
