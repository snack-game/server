package com.snackgame.server.member.business.domain;

import java.util.Objects;

import com.snackgame.server.member.business.exception.EmptyNameException;

public class Member {

    private final Long id;
    private String name;
    private Group group;

    public Member(String name) {
        this(null, name, null);
    }

    public Member(String name, Group group) {
        this(null, name, group);
    }

    public Member(Long id, String name, Group group) {
        validateNotNull(name);
        this.id = id;
        this.name = name;
        this.group = group;
    }

    public void changeNameTo(String name) {
        validateNotNull(name);
        this.name = name;
    }

    public void changeGroupTo(Group group) {
        this.group = group;
    }

    public void validateNotNull(String property) {
        if (Objects.isNull(property)) {
            throw new EmptyNameException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Group getGroup() {
        return group;
    }
}
