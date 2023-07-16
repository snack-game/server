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

import com.snackgame.server.applegame.business.domain.exception.GameSessionExpiredException;
import com.snackgame.server.common.domain.BaseEntity;
import com.snackgame.server.member.business.domain.Member;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game extends BaseEntity {

    public static final int DEFAULT_HEIGHT = 10;
    public static final int DEFAULT_WIDTH = 18;
    private static final int SESSION_SECONDS = 120;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member owner;
    @Embedded
    private Board board;
    private int score = 0;

    public Game(Board board, Member owner) {
        this(board, owner, now());
    }

    public Game(Board board, Member owner, LocalDateTime createdAt) {
        this.board = board;
        this.owner = owner;
        this.score = 0;
        this.createdAt = createdAt;
    }

    public static Game ofRandomized(Member owner) {
        return new Game(Board.ofRandomized(DEFAULT_HEIGHT, DEFAULT_WIDTH), owner);
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

    public boolean isOwner(Member member) {
        return owner.equals(member);
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
