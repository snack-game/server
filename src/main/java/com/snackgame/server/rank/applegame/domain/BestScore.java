package com.snackgame.server.rank.applegame.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    public BestScore(Long ownerId) {
        this.ownerId = ownerId;
    }

    public BestScore(int score, Long ownerId, Long sessionId) {
        this.score = score;
        this.ownerId = ownerId;
        this.sessionId = sessionId;
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
