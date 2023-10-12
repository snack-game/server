package com.snackgame.server.member.business.domain;

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
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Inheritance
@DiscriminatorColumn(name = "type")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @ManyToOne
    private Group group;

    public Member(Name name) {
        this.name = name;
    }

    public Member(Long id, Name name, Group group) {
        this.id = id;
        this.name = name;
        this.group = group;
    }

    public void changeNameTo(Name name) {
        this.name = name;
    }

    public void changeGroupTo(Group group) {
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getNameAsString() {
        return name.getString();
    }

    public AccountType getAccountType() {
        return AccountType.SELF;
    }

    public Group getGroup() {
        return group;
    }
}
