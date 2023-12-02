package com.snackgame.server.applegame.controller.dto;

import com.snackgame.server.applegame.domain.Coordinate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoordinateRequest {

    private Integer y;
    private Integer x;

    public Coordinate toCoordinate() {
        return new Coordinate(y, x);
    }
}
