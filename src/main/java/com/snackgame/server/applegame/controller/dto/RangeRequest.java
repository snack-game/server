package com.snackgame.server.applegame.controller.dto;

import com.snackgame.server.applegame.business.domain.Range;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RangeRequest {

    private CoordinateRequest topLeft;
    private CoordinateRequest bottomRight;

    public Range toRange() {
        return new Range(topLeft.toCoordinate(), bottomRight.toCoordinate());
    }
}
