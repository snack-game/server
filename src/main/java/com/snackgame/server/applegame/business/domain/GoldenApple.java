package com.snackgame.server.applegame.business.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonCreator;

public class GoldenApple extends AnyApple {

    private static final Map<Integer, GoldenApple> CACHE = new ConcurrentHashMap<>();

    private GoldenApple(int number) {
        super(number);
    }

    @JsonCreator
    public static GoldenApple of(int number) {
        if (!CACHE.containsKey(number)) {
            CACHE.put(number, new GoldenApple(number));
        }
        return CACHE.get(number);
    }

    @Override
    public GoldenApple golden() {
        return this;
    }

    @Override
    public boolean isGolden() {
        return true;
    }
}
