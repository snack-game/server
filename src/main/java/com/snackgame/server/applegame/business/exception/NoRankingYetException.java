package com.snackgame.server.applegame.business.exception;

public class NoRankingYetException extends AppleGameException {

    public NoRankingYetException() {
        super("아직 랭킹이 없습니다");
    }
}
