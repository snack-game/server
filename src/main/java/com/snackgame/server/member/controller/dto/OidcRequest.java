package com.snackgame.server.member.controller.dto;

import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OidcRequest {

    @Schema(example = "header.payload.signature")
    @Pattern(regexp = "^[A-Za-z0-9_-]+\\.([A-Za-z0-9_-]+\\.?)+[A-Za-z0-9_-]+$", message = "Id Token 형식이 틀렸습니다")
    private String idToken;
}
