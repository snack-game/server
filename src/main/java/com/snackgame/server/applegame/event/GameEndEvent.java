package com.snackgame.server.applegame.event;

import com.snackgame.server.applegame.domain.game.AppleGame;

public class GameEndEvent {

    private final AppleGame appleGame;

    public GameEndEvent(AppleGame appleGame) {
        this.appleGame = appleGame;
    }

    public AppleGame getAppleGame() {
        return appleGame;
    }
}
