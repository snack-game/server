package com.snackgame.server.applegame.business.domain;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.snackgame.server.applegame.business.exception.GameSessionExpiredException;
import com.snackgame.server.applegame.business.exception.NotOwnedException;
import com.snackgame.server.common.domain.BaseEntity;
import com.snackgame.server.member.business.domain.Member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleGame extends BaseEntity {

    public static final int DEFAULT_HEIGHT = 10;
    public static final int DEFAULT_WIDTH = 18;
    private static final int SESSION_SECONDS = 120;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;
    @ManyToOne
    private Member owner;
    @Embedded
    private Board board;
    private int score = 0;
    private boolean isEnded = false;

    public AppleGame(Board board, Member owner) {
        this(board, owner, now());
    }

    public AppleGame(Board board, Member owner, LocalDateTime createdAt) {
        this.board = board;
        this.owner = owner;
        this.score = 0;
        this.createdAt = createdAt;
    }

    public static AppleGame ofRandomized(Member owner) {
        return new AppleGame(Board.ofRandomized(DEFAULT_HEIGHT, DEFAULT_WIDTH), owner);
    }

    public void reset() {
        validateSessionAlive();
        this.board = Board.ofRandomized(DEFAULT_HEIGHT, DEFAULT_WIDTH);
        this.score = 0;
        this.updatedAt = now();
    }

    public void removeApplesIn(Range range) {
        validateSessionAlive();
        score += board.removeApplesIn(range);
    }

    public void validateOwnedBy(Member member) {
        if (!owner.equals(member)) {
            throw new NotOwnedException();
        }
    }

    public void end() {
        validateSessionAlive();
        this.isEnded = true;
    }

    public boolean isDone() {
        return isEnded || now().isAfter(createdAt.plusSeconds(SESSION_SECONDS));
    }

    private void validateSessionAlive() {
        if (this.isDone()) {
            throw new GameSessionExpiredException("게임 세션이 이미 종료되었습니다");
        }
    }

    public Long getSessionId() {
        return sessionId;
    }

    public List<List<Apple>> getApples() {
        return board.getApples();
    }

    public int getScore() {
        return score;
    }
}
