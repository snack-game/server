package com.snackgame.server.applegame.business.domain;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.snackgame.server.applegame.business.domain.exception.GameSessionExpiredException;
import com.snackgame.server.common.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game extends BaseEntity {

    private static final int DEFAULT_HEIGHT = 10;
    private static final int DEFAULT_WIDTH = 18;
    private static final int SESSION_SECONDS = 120;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Board board;
    private int score = 0;

    public Game(Board board) {
        this(board, now());
    }

    public Game(Board board, LocalDateTime createdAt) {
        this.board = board;
        this.score = 0;
        this.createdAt = createdAt;
    }

    public static Game ofRandomized() {
        return new Game(Board.ofRandomized(DEFAULT_HEIGHT, DEFAULT_WIDTH));
    }

    public void reset() {
        validateGameSessionAlive();
        this.board = Board.ofRandomized(DEFAULT_HEIGHT, DEFAULT_WIDTH);
        this.score = 0;
        this.updatedAt = now();
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

    public Long getId() {
        return id;
    }

    public List<List<Apple>> getApples() {
        return board.getApples();
    }

    public int getScore() {
        return score;
    }
}
