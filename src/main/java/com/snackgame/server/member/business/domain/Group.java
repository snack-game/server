package com.snackgame.server.member.business.domain;

public class Group {

    private final Long id;
    private final String name;

    public Group(String name) {
        this(null, name);
    }

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
