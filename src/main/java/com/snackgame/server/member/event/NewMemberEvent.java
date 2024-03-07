package com.snackgame.server.member.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NewMemberEvent {

    private final String name;

    public String getName() {
        return name;
    }
}
