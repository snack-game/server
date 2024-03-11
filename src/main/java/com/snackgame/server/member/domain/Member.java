package com.snackgame.server.member.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;

import com.snackgame.server.common.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(MemberEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Inheritance
@DiscriminatorColumn(name = "type")
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @ManyToOne
    private Group group;
    @Embedded
    private ProfileImage profileImage = ProfileImage.EMPTY;
    @Embedded
    private final Status status = new Status();

    private boolean isValid = true;

    public Member(Name name) {
        this.name = name;
    }

    public Member(Name name, ProfileImage profileImage) {
        this.name = name;
        this.profileImage = profileImage;
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

    public void changeProfileImageTo(ProfileImage profileImage) {
        this.profileImage = profileImage;
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
