package com.snackgame.server.applegame.business.rank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.snackgame.server.applegame.business.domain.AppleGame;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Entity
public class BestScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int score = 0;
    private Long ownerId = null;
    private Long sessionId = null;

    public BestScore() {
    }

    public BestScore(int score, Long ownerId, Long sessionId) {
        this.score = score;
        this.ownerId = ownerId;
        this.sessionId = sessionId;
    }

    public void renewWith(AppleGame appleGame) {
        if (appleGame.getScore() >= score) {
            this.score = appleGame.getScore();
            this.ownerId = appleGame.getOwner().getId();
            this.sessionId = appleGame.getSessionId();
        }
    }
}
