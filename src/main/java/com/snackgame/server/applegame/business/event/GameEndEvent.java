package com.snackgame.server.applegame.business.event;

import com.snackgame.server.applegame.business.domain.AppleGame;

public class GameEndEvent {

    private final AppleGame appleGame;

    public GameEndEvent(AppleGame appleGame) {
        this.appleGame = appleGame;
    }

    public AppleGame getAppleGame() {
        return appleGame;
    }
}
