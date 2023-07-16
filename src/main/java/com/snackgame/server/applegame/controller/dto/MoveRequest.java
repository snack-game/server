package com.snackgame.server.applegame.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MoveRequest {

    private Integer y;
    private Integer x;
}
