package com.snackgame.server.applegame.business.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ApplesFactory {

    public static List<List<Apple>> createRandomized(int height, int width) {
        if (anyNotPositive(height, width)) {
            return new ArrayList<>();
        }
        List<List<Apple>> apples = createdRowsOf(height, width);
        goldenOneIn(apples);
        return apples;
    }

    private static boolean anyNotPositive(int... numbers) {
        return Arrays.stream(numbers).anyMatch(number -> number <= 0);
    }

    private static List<List<Apple>> createdRowsOf(int height, int width) {
        List<List<Apple>> apples = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            apples.add(createRowOf(width));
        }
        return apples;
    }

    private static List<Apple> createRowOf(int width) {
        List<Apple> row = new ArrayList<>();
        for (int j = 0; j < width; j++) {
            row.add(Apple.ofRandomizedNumber());
        }
        return row;
    }

    private static void goldenOneIn(List<List<Apple>> apples) {
        Apple picked = pickOneIn(apples);

        apples.stream()
                .filter(row -> row.contains(picked))
                .findFirst()
                .ifPresent(row -> row.set(row.indexOf(picked), picked.golden()));
    }

    private static Apple pickOneIn(List<List<Apple>> apples) {
        List<Apple> targetRow = apples.get(pickIndexIn(apples.size()));
        return targetRow.get(pickIndexIn(targetRow.size()));
    }

    private static int pickIndexIn(int size) {
        return ThreadLocalRandom.current().nextInt(size);
    }
}
