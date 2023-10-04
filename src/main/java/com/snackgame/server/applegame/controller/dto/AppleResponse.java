package com.snackgame.server.applegame.controller.dto;

import java.util.List;

import com.snackgame.server.applegame.business.domain.Apple;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppleResponse {

    private final int number;
    private final boolean isGolden;

    public static List<AppleResponse> of(List<Apple> apples) {
        return apples.stream()
                .map(it -> new AppleResponse(it.getNumber(), it.isGolden()))
                .toList();
    }
}
