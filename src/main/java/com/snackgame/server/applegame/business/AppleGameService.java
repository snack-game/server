package com.snackgame.server.applegame.business;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.business.domain.AppleGame;
import com.snackgame.server.applegame.business.domain.AppleGameSessionRepository;
import com.snackgame.server.applegame.business.domain.Coordinate;
import com.snackgame.server.applegame.business.domain.Range;
import com.snackgame.server.applegame.business.exception.NoSuchSessionException;
import com.snackgame.server.applegame.controller.dto.MoveRequest;
import com.snackgame.server.member.business.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppleGameService {

    private final AppleGameSessionRepository appleGameSessions;

    @Transactional
    public AppleGame createFor(Member member) {
        AppleGame game = AppleGame.ofRandomized(member);
        return appleGameSessions.save(game);
    }

    @Transactional
    public void move(Member member, Long sessionId, List<MoveRequest> moves) {
        AppleGame game = findBy(sessionId);
        game.validateOwnedBy(member);
        Range range = new Range(toCoordinates(moves));
        game.removeApplesIn(range);
    }

    @Transactional
    public AppleGame resetBoard(Member member, Long sessionId) {
        AppleGame game = findBy(sessionId);
        game.validateOwnedBy(member);
        game.reset();
        return game;
    }

    @Transactional(readOnly = true)
    public AppleGame findBy(Long sessionId) {
        return appleGameSessions.findById(sessionId)
                .orElseThrow(NoSuchSessionException::new);
    }

    private List<Coordinate> toCoordinates(List<MoveRequest> moveRequests) {
        return moveRequests.stream()
                .map(moveRequest -> new Coordinate(moveRequest.getY(), moveRequest.getX()))
                .collect(Collectors.toList());
    }
}
