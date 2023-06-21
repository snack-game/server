package com.snackgame.server.member.domain;

import java.util.Random;

public class Guest extends Member {

    private static final String NAME_PREFIX = "게스트#";
    private static final int RANDOMIZED_LENGTH = 8;
    private static final String ALPHABET_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new Random();

    public Guest() {
        super(NAME_PREFIX + getRandomizedAlphabets(RANDOMIZED_LENGTH));
    }

    private static String getRandomizedAlphabets(int length) {
        StringBuilder randomized = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHABET_POOL.length());
            randomized.append(ALPHABET_POOL.charAt(index));
        }
        return randomized.toString();
    }
}
