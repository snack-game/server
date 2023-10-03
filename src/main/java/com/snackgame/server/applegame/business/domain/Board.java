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

    public int removeApplesIn(Range range) {
        validateContains(range);
        validateSumOf(range);
        int removed = 0;
        for (Coordinate coordinate : range.getAppleCoordinates()) {
            removeAppleAt(coordinate);
            ++removed;
        }
        return removed;
    }

    private void validateContains(Range range) {
        boolean appleDoesNotExist = getApplesIn(range.getAppleCoordinates()).stream()
                .anyMatch(Apple::isEmpty);
        boolean unexpectedAppleExists = getApplesIn(range.getEmptyCoordinates()).stream()
                .anyMatch(Apple::exists);
        if (appleDoesNotExist || unexpectedAppleExists) {
            throw new InvalidRangeException();
        }
    }

    private void validateSumOf(Range range) {
        if (sumApplesIn(range) != REMOVABLE_SUM) {
            throw new AppleNotRemovableException("사과들의 합이 " + REMOVABLE_SUM + "이 아닙니다");
        }
    }

    private int sumApplesIn(Range range) {
        return getApplesIn(range.getAppleCoordinates()).stream()
                .map(Apple::getNumber)
                .reduce(0, Integer::sum);
    }

    private List<Apple> getApplesIn(List<Coordinate> coordinates) {
        return coordinates.stream()
                .map(coordinate -> apples.get(coordinate.getY()).get(coordinate.getX()))
                .collect(Collectors.toList());
    }

    private void removeAppleAt(Coordinate coordinate) {
        List<Apple> row = apples.get(coordinate.getY());
        row.set(coordinate.getX(), Apple.EMPTY);
    }

    public List<List<Apple>> getApples() {
        return apples.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }
}
