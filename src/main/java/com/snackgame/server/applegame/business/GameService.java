package com.snackgame.server.applegame.business;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.applegame.business.domain.Game;
import com.snackgame.server.applegame.business.domain.GameRepository;
import com.snackgame.server.member.business.domain.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository games;

    @Transactional
    public Game createGameFor(Member member) {
        Game game = Game.ofRandomized(member);
        games.save(game);
        return game;
    }
}
