package com.snackgame.server.applegame.business;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.annotation.ServiceTest;
import com.snackgame.server.applegame.business.domain.Game;
import com.snackgame.server.member.business.MemberService;
import com.snackgame.server.member.business.domain.Member;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ServiceTest
class GameServiceTest {

    @Autowired
    GameService gameService;
    @Autowired
    MemberService memberService;

    @Test
    void 게임을_시작한다() {
        Member owner = memberService.createGuest();

        Game game = gameService.createGameFor(owner);

        assertThat(game.getId()).isNotNull();
        assertThat(game.getApples()).hasSize(Game.DEFAULT_HEIGHT);
        assertThat(game.getApples().get(0)).hasSize(Game.DEFAULT_WIDTH);
    }
}
