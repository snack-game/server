package com.snackgame.server.applegame.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.snackgame.server.applegame.business.domain.Coordinate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MoveRequest {

    private List<CoordinateRequest> coordinates;

    public List<Coordinate> toCoordinates() {
        return coordinates.stream()
                .map(CoordinateRequest::toCoordinate)
                .collect(Collectors.toList());
    }
}
