package com.snackgame.server.applegame.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.snackgame.server.applegame.business.domain.Apple;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppleResponse {

    private final int number;
    private final boolean isGolden;

    public static List<List<AppleResponse>> of(List<List<Apple>> apples) {
        return apples.stream()
                .map(AppleResponse::ofRow)
                .collect(Collectors.toList());
    }

    private static List<AppleResponse> ofRow(List<Apple> apples) {
        return apples.stream()
                .map(it -> new AppleResponse(it.getNumber(), it.isGolden()))
                .collect(Collectors.toList());
    }
}
