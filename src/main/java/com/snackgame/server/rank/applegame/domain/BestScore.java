package com.snackgame.server.rank.applegame.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.snackgame.server.applegame.domain.game.AppleGame;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(uniqueConstraints = @UniqueConstraint(
        name = "unique_best_score_in_a_season",
        columnNames = {"season_id", "owner_id"}
))
@Entity
public class BestScore {

    public static final BestScore EMPTY = new BestScore();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double score = 0;
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
    @Column(name = "season_id", nullable = false)
    private Long seasonId;
    private Long sessionId = null;

    public BestScore(Long ownerId, Long seasonId) {
        this.ownerId = ownerId;
        this.seasonId = seasonId;
    }

    public BestScore(int score, Long ownerId, Long seasonId, Long sessionId) {
        this.score = score;
        this.ownerId = ownerId;
        this.seasonId = seasonId;
        this.sessionId = sessionId;
    }

    public void renewWith(AppleGame appleGame) {
        if (appleGame.getScore() >= score) {
            this.score = appleGame.getScore();
            this.sessionId = appleGame.getSessionId();
        }
    }

    public boolean beats(BestScore other) {
        return !this.equals(EMPTY) && this.score >= other.score;
    }

    public void transferTo(Long ownerId) {
        this.ownerId = ownerId;
    }
}
