package com.snackgame.server.applegame.business.domain;

import java.util.ArrayList;
import java.util.List;

public class Range {

    private final List<Coordinate> appleCoordinates;

    public Range(List<Coordinate> appleCoordinates) {
        this.appleCoordinates = new ArrayList<>(appleCoordinates);
    }

    public Coordinate getTopLeft() {
        int minY = appleCoordinates.stream()
                .mapToInt(Coordinate::getY)
                .min().orElse(0);
        int minX = appleCoordinates.stream()
                .mapToInt(Coordinate::getX)
                .min().orElse(0);
        return new Coordinate(minY, minX);
    }

    public Coordinate getBottomRight() {
        int maxY = appleCoordinates.stream()
                .mapToInt(Coordinate::getY)
                .max().orElse(0);
        int maxX = appleCoordinates.stream()
                .mapToInt(Coordinate::getX)
                .max().orElse(0);
        return new Coordinate(maxY, maxX);
    }

    public List<Coordinate> getCompleteCoordinates() {
        Coordinate topLeft = getTopLeft();
        Coordinate bottomRight = getBottomRight();
        List<Coordinate> completeCoordinates = new ArrayList<>();
        for (int y = topLeft.getY(); y <= bottomRight.getY(); y++) {
            for (int x = topLeft.getX(); x <= bottomRight.getX(); x++) {
                completeCoordinates.add(new Coordinate(y, x));
            }
        }
        return completeCoordinates;
    }

    public List<Coordinate> getAppleCoordinates() {
        return new ArrayList<>(appleCoordinates);
    }

    public List<Coordinate> getEmptyCoordinates() {
        List<Coordinate> emptyCoordinates = getCompleteCoordinates();
        emptyCoordinates.removeAll(getAppleCoordinates());
        return emptyCoordinates;
    }
}
