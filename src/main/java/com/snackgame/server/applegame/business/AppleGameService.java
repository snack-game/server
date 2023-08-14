package com.snackgame.server.applegame.business;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

@RequiredArgsConstructor
@Transactional
@Service
public class AppleGameService {

    private final AppleGameSessionRepository appleGameSessions;

    public AppleGame startGameOf(Member member) {
        AppleGame game = AppleGame.ofRandomized(member);
        return appleGameSessions.save(game);
    }

    public void placeMoves(Member member, Long sessionId, List<MoveRequest> moves) {
        AppleGame game = findBy(sessionId);
        game.validateOwnedBy(member);
        Range range = new Range(toCoordinates(moves));
        game.removeApplesIn(range);
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

    @Transactional(readOnly = true)
    public List<AppleGame> getEndedGamesAt(int page, int size) {
        return appleGameSessions.findAllByIsEndedIsTrue(
                PageRequest.of(page, size, Sort.Direction.DESC, "score")
        );
    }

    private List<Coordinate> toCoordinates(List<MoveRequest> moveRequests) {
        return moveRequests.stream()
                .map(moveRequest -> new Coordinate(moveRequest.getY(), moveRequest.getX()))
                .collect(Collectors.toList());
    }
}
