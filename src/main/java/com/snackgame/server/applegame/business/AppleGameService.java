package com.snackgame.server.applegame.business;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.business.domain.AppleGameSessionRepository;
import com.snackgame.server.applegame.business.domain.Board;
import com.snackgame.server.applegame.business.exception.NoSuchSessionException;
import com.snackgame.server.applegame.controller.dto.MoveRequest;
import com.snackgame.server.member.business.domain.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AppleGameService {

    private final AppleGameSessionRepository appleGameSessions;

    public AppleGame startGameOf(Member member) {
        AppleGame game = AppleGame.ofRandomized(member);
        return appleGameSessions.save(game);
    }

    @Deprecated(forRemoval = true)
    public void placeMovesV1(Member member, Long sessionId, List<MoveRequest> moves) {
        AppleGame game = findBy(sessionId);
        game.validateOwnedBy(member);

        moves.forEach(move -> game.removeApplesInV1(move.toCoordinates()));
    }

    public Optional<AppleGame> placeMoves(Member member, Long sessionId, List<MoveRequest> moves) {
        AppleGame game = findBy(sessionId);
        game.validateOwnedBy(member);

        Board previous = game.getBoard();
        moves.forEach(move -> game.removeApplesIn(move.toCoordinates()));

        if (!game.getBoard().equals(previous)) {
            return Optional.of(game);
        }
        return Optional.empty();
    }

    public AppleGame resetBoard(Member member, Long sessionId) {
        AppleGame game = findBy(sessionId);
        game.validateOwnedBy(member);
        game.reset();
        return game;
    }

    public void endSession(Member member, Long sessionId) {
        AppleGame game = findBy(sessionId);
        game.validateOwnedBy(member);
        game.end();
    }

    @Transactional(readOnly = true)
    public AppleGame findBy(Long sessionId) {
        return appleGameSessions.findById(sessionId)
                .orElseThrow(NoSuchSessionException::new);
    }
}
