package com.snackgame.server.applegame.domain.game;

import static java.time.LocalDateTime.now;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.snackgame.server.applegame.domain.Range;
import com.snackgame.server.applegame.domain.apple.Apple;
import com.snackgame.server.applegame.exception.GameSessionExpiredException;
import com.snackgame.server.common.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class AppleGame extends BaseEntity {

    public static final int DEFAULT_HEIGHT = 10;
    public static final int DEFAULT_WIDTH = 12;
    private static final Duration SPARE_TIME = Duration.ofSeconds(5);
    private static final Duration SESSION_TIME = Duration.ofSeconds(120).plus(SPARE_TIME);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
    private Long ownerId;
    @Lob
    private Board board;
    private int score = 0;
    private LocalDateTime finishedAt;

    public AppleGame(Board board, Long ownerId) {
        this.board = board;
        this.ownerId = ownerId;
        this.finishedAt = willFinishAt();
    }

    public AppleGame(Board board, Long ownerId, LocalDateTime finishedAt) {
        this.board = board;
        this.ownerId = ownerId;
        this.finishedAt = finishedAt;
    }

    public AppleGame(Board board, Long ownerId, LocalDateTime finishedAt, int score) {
        this.board = board;
        this.ownerId = ownerId;
        this.finishedAt = finishedAt;
        this.score = score;
    }

    public static AppleGame ofRandomized(Long ownerId) {
        return new AppleGame(new Board(DEFAULT_HEIGHT, DEFAULT_WIDTH), ownerId);
    }

    public void restart() {
        validateOngoing();
        this.board = board.reset();
        this.score = 0;
        this.createdAt = now();
        this.finishedAt = willFinishAt();
    }

    public void removeApplesIn(Range range) {
        validateOngoing();
        List<Apple> removed = board.removeApplesIn(range);
        if (removed.stream().anyMatch(Apple::isGolden)) {
            board = board.reset();
        }
        score += removed.size();
    }

    public void finish() {
        validateOngoing();
        this.finishedAt = now();
    }

    private void validateOngoing() {
        if (this.isFinished()) {
            throw new GameSessionExpiredException("이미 종료된 게임입니다");
        }
    }

    public boolean isFinished() {
        LocalDateTime now = now();
        return now.isEqual(finishedAt) || now.isAfter(finishedAt);
    }

    private LocalDateTime willFinishAt() {
        return createdAt.plus(SESSION_TIME);
    }

    public List<List<Apple>> getApples() {
        return board.getApples();
    }
}
