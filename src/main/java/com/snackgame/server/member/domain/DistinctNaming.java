package com.snackgame.server.member.domain;

import org.springframework.stereotype.Component;

import com.snackgame.server.member.exception.DuplicateNameException;

@Component
public class DistinctNaming {

    private final MemberRepository members;
    private final GuestNameRandomizer guestNameRandomizer = new GuestNameRandomizer();

    public DistinctNaming(MemberRepository members) {
        this.members = members;
    }

    public void validate(Name name) {
        if (members.existsByName(name)) {
            throw new DuplicateNameException();
        }
    }

    public Name ofGuest() {
        Name name = guestNameRandomizer.get();
        while (members.existsByName(name)) {
            name = guestNameRandomizer.get();
        }
        return name;
    }

    public Name from(Name name) {
        while (members.existsByName(name)) {
            name = name.nextAvailable();
        }
        return name;
    }
}
