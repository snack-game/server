package com.snackgame.server.rank.applegame.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.snackgame.server.applegame.domain.game.AppleGame;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BestScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int score = 0;
    private Long ownerId = null;
    private Long sessionId = null;
    private Long seasonId;

    public BestScore(Long ownerId) {
        this.ownerId = ownerId;
    }

    public BestScore(int score, Long ownerId, Long sessionId, Long seasonId) {
        this.score = score;
        this.ownerId = ownerId;
        this.sessionId = sessionId;
        this.seasonId = seasonId;
    }

    public void renewWith(AppleGame appleGame) {
        if (appleGame.getScore() >= score) {
            this.score = appleGame.getScore();
            this.sessionId = appleGame.getSessionId();
        }
    }

    public void overwriteWith(BestScore other) {
        this.score = other.getScore();
        this.sessionId = other.getSessionId();
    }

    public boolean beats(BestScore other) {
        return this.score >= other.score;
    }
}
