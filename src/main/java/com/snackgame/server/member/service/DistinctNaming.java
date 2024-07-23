package com.snackgame.server.member.service;

import org.springframework.stereotype.Component;

import com.snackgame.server.member.domain.MemberRepository;
import com.snackgame.server.member.domain.Name;
import com.snackgame.server.member.exception.DuplicateNameException;

@Component
public class DistinctNaming {

    private final MemberRepository members;
    private final GuestNameRandomizer guestNameRandomizer;

    public DistinctNaming(MemberRepository members, GuestNameRandomizer guestNameRandomizer) {
        this.members = members;
        this.guestNameRandomizer = guestNameRandomizer;
    }

    public void validate(Name name) {
        if (members.existsByName(name)) {
            throw new DuplicateNameException();
        }
    }

    public Name ofGuest() {
        Name name = guestNameRandomizer.getWith("guest");
        while (members.existsByName(name)) {
            name = guestNameRandomizer.getWith("guest");
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
