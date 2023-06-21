package com.snackgame.server.member.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberRequest {

    private String name;
    private String group;
}
