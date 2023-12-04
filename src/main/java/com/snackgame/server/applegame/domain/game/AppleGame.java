package com.snackgame.server.applegame.domain.game;

import static java.time.LocalDateTime.now;

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
import com.snackgame.server.applegame.exception.NotOwnedException;
import com.snackgame.server.common.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleGame extends BaseEntity {

    public static final int DEFAULT_HEIGHT = 10;
    public static final int DEFAULT_WIDTH = 12;
    private static final int SESSION_SECONDS = 120;
    private static final int SPARE_SECONDS = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
    private Long ownerId;
    @Lob
    private Board board;
    private int score = 0;
    private boolean isFinished = false;

    public AppleGame(Board board, Long ownerId) {
        this.board = board;
        this.ownerId = ownerId;
    }

    public AppleGame(Board board, Long ownerId, LocalDateTime createdAt) {
        this.board = board;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
    }

    public static AppleGame ofRandomized(Long ownerId) {
        return new AppleGame(new Board(DEFAULT_HEIGHT, DEFAULT_WIDTH), ownerId);
    }

    public void validateOwnedBy(Long memberId) {
        if (!ownerId.equals(memberId)) {
            throw new NotOwnedException();
        }
    }

    public void restart() {
        validateOngoing();
        this.board = board.reset();
        this.score = 0;
        this.createdAt = now();
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
        this.isFinished = true;
    }

    public boolean isFinished() {
        return isFinished || now().isAfter(createdAt.plusSeconds(SESSION_SECONDS).plusSeconds(SPARE_SECONDS));
    }

    private void validateOngoing() {
        if (this.isFinished()) {
            throw new GameSessionExpiredException("이미 종료된 게임입니다");
        }
    }

    public Long getSessionId() {
        return sessionId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public List<List<Apple>> getApples() {
        return board.getApples();
    }

    public int getScore() {
        return score;
    }

    public Board getBoard() {
        return board;
    }
}
