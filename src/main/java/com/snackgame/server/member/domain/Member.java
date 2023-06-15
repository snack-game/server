package com.snackgame.server.member.domain;

public class Member {

    private final Long id;
    private String name;
    private String groupName;

    public Member(String name, String groupName) {
        this(null, name, groupName);
    }

    public Member(Long id, String name, String groupName) {
        this.id = id;
        this.name = name;
        this.groupName = groupName;
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
