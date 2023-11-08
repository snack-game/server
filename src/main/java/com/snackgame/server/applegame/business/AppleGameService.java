package com.snackgame.server.applegame.business;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.business.domain.AppleGameSessionRepository;
import com.snackgame.server.applegame.business.domain.Board;
import com.snackgame.server.applegame.business.event.GameEndEvent;
import com.snackgame.server.applegame.controller.dto.RangeRequest;
import com.snackgame.server.member.business.domain.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AppleGameService {

    private final AppleGameSessionRepository sessions;
    private final ApplicationEventPublisher eventPublisher;

    public AppleGame startGameOf(Member member) {
        AppleGame game = AppleGame.ofRandomized(member);
        return sessions.save(game);
    }

    public Optional<AppleGame> placeMoves(Member member, Long sessionId, List<RangeRequest> rangeRequests) {
        AppleGame game = sessions.getBy(sessionId);
        game.validateOwnedBy(member);

        Board previous = game.getBoard();
        rangeRequests.forEach(request -> game.removeApplesIn(request.toRange()));

        if (!game.getBoard().equals(previous)) {
            return Optional.of(game);
        }
        return Optional.empty();
    }

    public AppleGame resetBoard(Member member, Long sessionId) {
        AppleGame game = sessions.getBy(sessionId);
        game.validateOwnedBy(member);
        game.reset();
        return game;
    }

    public void endSession(Member member, Long sessionId) {
        AppleGame game = sessions.getBy(sessionId);
        game.validateOwnedBy(member);
        game.end();
        eventPublisher.publishEvent(new GameEndEvent(game));
    }
}
