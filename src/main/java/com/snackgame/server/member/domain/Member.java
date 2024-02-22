package com.snackgame.server.member.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;

import com.snackgame.server.common.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Inheritance
@DiscriminatorColumn(name = "type")
@Getter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @ManyToOne
    private Group group;
    @Embedded
    private Status status;

    private boolean isValid = true;

    public Member(Name name, Status status) {
        this.name = name;
        this.status = status;
    }

    public Member(Long id, Name name, Status status, Group group) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.group = group;
    }

    public void changeNameTo(Name name) {
        this.name = name;
    }

    public void changeGroupTo(Group group) {
        this.group = group;
    }

    public void changeStatusTo(Status status) {
        this.status = status;
    }

    public void invalidate() {
        this.isValid = false;
    }

    public String getNameAsString() {
        return name.getString();
    }

    public AccountType getAccountType() {
        return AccountType.SELF;
    }
}
