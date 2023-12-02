package com.snackgame.server.applegame.domain;

import java.util.ArrayList;
import java.util.List;

public class Range {

    private final Coordinate topLeft;
    private final Coordinate bottomRight;

    public Range(Coordinate topLeft, Coordinate bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public List<Coordinate> getCompleteCoordinates() {
        List<Coordinate> completeCoordinates = new ArrayList<>();
        for (int y = topLeft.getY(); y <= bottomRight.getY(); y++) {
            for (int x = topLeft.getX(); x <= bottomRight.getX(); x++) {
                completeCoordinates.add(new Coordinate(y, x));
            }
        }
        return completeCoordinates;
    }
}
