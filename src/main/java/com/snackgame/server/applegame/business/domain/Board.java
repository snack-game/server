package com.snackgame.server.applegame.business.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.snackgame.server.applegame.business.domain.exception.AppleNotRemovableException;
import com.snackgame.server.applegame.business.domain.exception.InvalidRangeException;

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
            List<Apple> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(Apple.ofRandomizedNumber());
            }
            apples.add(row);
        }
        return new Board(apples);
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