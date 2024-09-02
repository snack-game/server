package com.snackgame.server.applegame.domain.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snackgame.server.applegame.domain.Coordinate;
import com.snackgame.server.applegame.domain.Range;
import com.snackgame.server.applegame.domain.apple.Apple;
import com.snackgame.server.applegame.domain.apple.EmptyApple;
import com.snackgame.server.applegame.exception.AppleNotRemovableException;
import com.snackgame.server.applegame.exception.InvalidBoardSizeException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleGameBoard {

    private static final int REMOVABLE_SUM = 10;

    private List<List<Apple>> apples;

    public AppleGameBoard(List<List<Apple>> apples) {
        validateSquared(apples);
        this.apples = apples.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    public AppleGameBoard(int height, int width) {
        this(ApplesFactory.createRandomized(height, width));
    }

    private void validateSquared(List<List<Apple>> apples) {
        if (apples.isEmpty() || apples.get(0).isEmpty()) {
            throw new InvalidBoardSizeException();
        }
    }

    public AppleGameBoard reset() {
        return new AppleGameBoard(this.getHeight(), this.getWidth());
    }

    protected List<Apple> removeApplesIn(Range range) {
        validateSumOf(range.getCompleteCoordinates());
        return range.getCompleteCoordinates().stream()
                .map(this::removeAppleAt)
                .filter(Apple::exists)
                .collect(Collectors.toList());
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
        List<Apple> row = apples.get(coordinate.getY());
        return row.set(coordinate.getX(), EmptyApple.get());
    }

    public List<List<Apple>> getApples() {
        return apples.stream()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    public int getHeight() {
        return apples.size();
    }

    @JsonIgnore
    public int getWidth() {
        return apples.get(0).size();
    }
}
