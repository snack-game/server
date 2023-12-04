package com.snackgame.server.member.domain;

import java.util.concurrent.ThreadLocalRandom;

public class GuestNameRandomizer {

    private static final String NAME_PREFIX = "게스트#";
    private static final int RANDOMIZED_LENGTH = 8;
    private static final String ALPHABET_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static String getRandomizedAlphabets(int length) {
        StringBuilder randomized = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(ALPHABET_POOL.length());
            randomized.append(ALPHABET_POOL.charAt(index));
        }
        return randomized.toString();
    }

    public Name get() {
        return new Name(NAME_PREFIX + getRandomizedAlphabets(RANDOMIZED_LENGTH));
    }
}
