package com.snackgame.server.applegame.business.domain.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.snackgame.server.applegame.business.domain.apple.Apple;
import com.snackgame.server.applegame.business.domain.apple.PlainApple;

public class ApplesFactory {

    public static List<List<Apple>> createRandomized(int height, int width) {
        if (allPositive(height, width)) {
            List<List<Apple>> apples = createRows(height, width);
            goldenOneIn(apples);
            return apples;
        }
        return new ArrayList<>();
    }

    private static List<List<Apple>> createRows(int height, int width) {
        List<List<Apple>> apples = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            apples.add(createRowOf(width));
        }
        return apples;
    }

    private static List<Apple> createRowOf(int width) {
        List<Apple> row = new ArrayList<>();
        for (int j = 0; j < width; j++) {
            row.add(PlainApple.ofRandomizedNumber());
        }
        return row;
    }

    private static void goldenOneIn(List<List<Apple>> apples) {
        int row = pickIndexIn(apples.size());
        int column = pickIndexIn(apples.get(row).size());
        Apple picked = apples.get(row).get(column);

        apples.get(row).set(column, picked.golden());
    }

    private static int pickIndexIn(int size) {
        return ThreadLocalRandom.current().nextInt(size);
    }

    private static boolean allPositive(int... numbers) {
        return Arrays.stream(numbers).allMatch(number -> number > 0);
    }
}
