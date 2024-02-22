package com.snackgame.server.member.domain;

import javax.persistence.Entity;

import com.snackgame.server.member.exception.GuestRestrictedException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guest extends Member {

    public Guest(Name name, Status status) {
        super(name, status);
    }

    @Override
    public void changeNameTo(Name name) {
        throw new GuestRestrictedException();
    }

    @Override
    public void changeGroupTo(Group group) {
        throw new GuestRestrictedException();
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.GUEST;
    }
}
