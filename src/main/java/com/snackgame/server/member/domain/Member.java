package com.snackgame.server.member.domain;

import java.util.Objects;

import com.snackgame.server.member.exception.EmptyNameException;

public class Member {

    private final Long id;
    private String name;
    private String groupName;

    public Member(String name) {
        this(null, name, null);
    }

    public Member(String name, String groupName) {
        this(null, name, groupName);
    }

    public Member(Long id, String name, String groupName) {
        validateNotNull(name);
        this.id = id;
        this.name = name;
        this.groupName = groupName;
    }

    public void validateNotNull(String property) {
        if (Objects.isNull(property)) {
            throw new EmptyNameException();
        }
    }

    public void changeNameTo(String name) {
        this.name = name;
    }

    public void changeGroupNameTo(String group) {
        this.groupName = group;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGroupName() {
        return groupName;
    }
}
