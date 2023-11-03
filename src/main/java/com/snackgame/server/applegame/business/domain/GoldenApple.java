package com.snackgame.server.applegame.business.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GoldenApple extends Apple {

    private static final Map<Integer, GoldenApple> CACHE = new ConcurrentHashMap<>();

    private GoldenApple(int number) {
        super(number);
    }

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
