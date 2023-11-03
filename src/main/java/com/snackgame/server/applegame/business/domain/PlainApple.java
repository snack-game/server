package com.snackgame.server.applegame.business.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PlainApple extends Apple {

    private static final Map<Integer, PlainApple> CACHE = new ConcurrentHashMap<>();

    private PlainApple(int number) {
        super(number);
    }

    public static PlainApple of(int number) {
        if (!CACHE.containsKey(number)) {
            CACHE.put(number, new PlainApple(number));
        }
        return CACHE.get(number);
    }

    public static PlainApple ofRandomizedNumber() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int randomizedNumber = random.nextInt(NUMBER_MINIMUM, NUMBER_MAXIMUM + 1);
        return of(randomizedNumber);
    }

    public GoldenApple golden() {
        return GoldenApple.of(getNumber());
    }

    public boolean isGolden() {
        return false;
    }

    public boolean exists() {
        return true;
    }
}
