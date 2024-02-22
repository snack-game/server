package com.snackgame.server.applegame;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.controller.dto.RangeRequest;
import com.snackgame.server.applegame.domain.game.AppleGame;
import com.snackgame.server.applegame.domain.game.AppleGames;
import com.snackgame.server.applegame.domain.game.Board;
import com.snackgame.server.applegame.event.GameEndEvent;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class AppleGameService {

    private final AppleGames appleGames;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberRepository memberRepository;

    public AppleGame startGameFor(Long memberId) {
        AppleGame game = AppleGame.ofRandomized(memberId);
        return appleGames.save(game);
    }

    public Optional<AppleGame> placeMoves(Long memberId, Long sessionId, List<RangeRequest> rangeRequests) {
        AppleGame game = appleGames.getBy(memberId, sessionId);
        Board previous = game.getBoard();

        rangeRequests.forEach(request -> game.removeApplesIn(request.toRange()));

        if (!game.getBoard().equals(previous)) {
            return Optional.of(game);
        }
        return Optional.empty();
    }

    public AppleGame restart(Long memberId, Long sessionId) {
        AppleGame game = appleGames.getBy(memberId, sessionId);
        game.restart();
        return game;
    }

    public void finish(Long memberId, Long sessionId) {
        AppleGame game = appleGames.getBy(memberId, sessionId);
        game.finish();
        Member member = memberRepository.getById(memberId);
        member.getStatus().addExp(game.getScore());// 안전 <-> 변경 용이성
        eventPublisher.publishEvent(new GameEndEvent(game));
    }
}
