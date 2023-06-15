package com.snackgame.server.applegame.domain;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;
import java.util.List;

import com.snackgame.server.applegame.domain.exception.GameSessionExpiredException;

public class Game {

    private static final int DEFAULT_HEIGHT = 10;
    private static final int DEFAULT_WIDTH = 18;
    private static final int SESSION_SECONDS = 120;

    private Board board;
    private int score;
    private LocalDateTime createdAt;

    public Game() {
        this(Board.ofRandomized(DEFAULT_HEIGHT, DEFAULT_WIDTH), 0, now());
    }

    public Game(Board board, int score, LocalDateTime createdAt) {
        this.board = board;
        this.score = score;
        this.createdAt = createdAt;
    }

    public void reset() {
        validateGameSessionAlive();
        this.board = Board.ofRandomized(DEFAULT_HEIGHT, DEFAULT_WIDTH);
        this.score = 0;
        this.createdAt = now();
    }

    public void removeApplesIn(Range range) {
        validateGameSessionAlive();
        score += board.removeApplesIn(range);
    }

    private void validateGameSessionAlive() {
        if (now().isAfter(createdAt.plusSeconds(SESSION_SECONDS))) {
            throw new GameSessionExpiredException("게임 세션이 이미 종료되었습니다");
        }
    }

    public List<List<Apple>> getApples() {
        return board.getApples();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getScore() {
        return score;
    }
}
