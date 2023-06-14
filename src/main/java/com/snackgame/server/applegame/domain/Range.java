package com.snackgame.server.applegame.domain;

import java.util.ArrayList;
import java.util.List;

public class Range {

    private final List<Coordinate> coordinates;

    public Range(List<Coordinate> coordinates) {
        this.coordinates = new ArrayList<>(coordinates);
    }

    public Coordinate getTopLeft() {
        int minY = coordinates.stream()
                .mapToInt(Coordinate::getY)
                .min().orElse(0);
        int minX = coordinates.stream()
                .mapToInt(Coordinate::getX)
                .min().orElse(0);
        return new Coordinate(minY, minX);
    }

    public Coordinate getBottomRight() {
        int maxY = coordinates.stream()
                .mapToInt(Coordinate::getY)
                .max().orElse(0);
        int maxX = coordinates.stream()
                .mapToInt(Coordinate::getX)
                .max().orElse(0);
        return new Coordinate(maxY, maxX);
    }

    public List<Coordinate> getCoordinates() {
        return new ArrayList<>(coordinates);
    }
}
