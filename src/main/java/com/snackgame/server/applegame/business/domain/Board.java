package com.snackgame.server.applegame.business.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import com.snackgame.server.applegame.business.exception.AppleNotRemovableException;
import com.snackgame.server.applegame.business.exception.InvalidRangeException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    private static final int REMOVABLE_SUM = 10;

    @Lob
    @Column(nullable = false)
    private List<List<Apple>> apples;

    public Board(List<List<Apple>> apples) {
        this.apples = apples.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    public static Board ofRandomized(int height, int width) {
        List<List<Apple>> apples = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            apples.add(createRowOf(width));
        }
        Apple picked = pickOneIn(apples);
        apples.stream()
                .filter(row -> row.contains(picked))
                .findFirst()
                .ifPresent(row -> golden(row, picked));
        return new Board(apples);
    }

    private static List<Apple> createRowOf(int width) {
        List<Apple> row = new ArrayList<>();
        for (int j = 0; j < width; j++) {
            row.add(Apple.ofRandomizedNumber());
        }
        return row;
    }

    private static Apple pickOneIn(List<List<Apple>> apples) {
        List<Apple> targetRow = apples.get(pickIndexIn(apples.size()));
        return targetRow.get(pickIndexIn(targetRow.size()));
    }

    private static int pickIndexIn(int size) {
        return ThreadLocalRandom.current().nextInt(size);
    }

    private static void golden(List<Apple> apples, Apple apple) {
        apples.set(apples.indexOf(apple), apple.golden());
    }

    public int removeApplesIn(List<Coordinate> coordinates) {
        validateSumOf(coordinates);
        int removed = 0;
        for (Coordinate coordinate : coordinates) {
            removeAppleAt(coordinate);
            ++removed;
        }
        return removed;
    }

    private void validateSumOf(List<Coordinate> coordinates) {
        if (sumApplesIn(coordinates) != REMOVABLE_SUM) {
            throw new AppleNotRemovableException("사과들의 합이 " + REMOVABLE_SUM + "이 아닙니다");
        }
    }

    private int sumApplesIn(List<Coordinate> coordinates) {
        return getApplesIn(coordinates).stream()
                .map(Apple::getNumber)
                .reduce(0, Integer::sum);
    }

    private List<Apple> getApplesIn(List<Coordinate> coordinates) {
        return coordinates.stream()
                .map(coordinate -> apples.get(coordinate.getY()).get(coordinate.getX()))
                .collect(Collectors.toList());
    }

    private void removeAppleAt(Coordinate coordinate) {
        validateAppleIsAt(coordinate);
        List<Apple> row = apples.get(coordinate.getY());
        row.set(coordinate.getX(), Apple.EMPTY);
    }

    private void validateAppleIsAt(Coordinate coordinate) {
        Apple apple = apples.get(coordinate.getY()).get(coordinate.getX());
        if (apple.isEmpty()) {
            throw new InvalidRangeException();
        }
    }

    public List<List<Apple>> getApples() {
        return apples.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }
}
