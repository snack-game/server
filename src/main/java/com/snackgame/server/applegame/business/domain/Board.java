package com.snackgame.server.applegame.business.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import com.snackgame.server.applegame.business.exception.AppleNotRemovableException;
import com.snackgame.server.applegame.business.exception.InvalidBoardSizeException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    private static final int MIN_HEIGHT = 1;
    private static final int MIN_WIDTH = 1;
    private static final int REMOVABLE_SUM = 10;

    @Lob
    @Column(nullable = false)
    private List<List<Apple>> apples;

    public Board(List<List<Apple>> apples) {
        validateSizeOf(apples);
        this.apples = apples.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    public Board(int height, int width) {
        this(ApplesFactory.createRandomized(height, width));
    }

    private void validateSizeOf(List<List<Apple>> apples) {
        if (apples.size() < MIN_HEIGHT || apples.get(0).size() < MIN_WIDTH) {
            throw new InvalidBoardSizeException();
        }
    }

    public Board reset() {
        return new Board(this.getHeight(), this.getWidth());
    }

    protected List<Apple> removeApplesIn(List<Coordinate> coordinates) {
        List<Apple> removed = new ArrayList<>();
        validateSumOf(coordinates);
        for (Coordinate coordinate : coordinates) {
            removed.add(removeAppleAt(coordinate));
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

    private Apple removeAppleAt(Coordinate coordinate) {
        validateAppleIsAt(coordinate);
        List<Apple> row = apples.get(coordinate.getY());
        return row.set(coordinate.getX(), Apple.EMPTY);
    }

    private void validateAppleIsAt(Coordinate coordinate) {
        Apple apple = apples.get(coordinate.getY()).get(coordinate.getX());
        if (apple.isEmpty()) {
            throw new AppleNotRemovableException("없는 사과를 제거하려고 했습니다");
        }
    }

    public List<List<Apple>> getApples() {
        return apples.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    public int getHeight() {
        return apples.size();
    }

    public int getWidth() {
        return apples.get(0).size();
    }
}
