package com.snackgame.server.member.service;

import org.springframework.stereotype.Component;

import com.snackgame.server.member.domain.MemberRepository;
import com.snackgame.server.member.domain.Name;
import com.snackgame.server.member.exception.DuplicateNameException;

@Component
public class DistinctNaming {

    private final MemberRepository members;
    private final NameRandomizer nameRandomizer;

    public DistinctNaming(MemberRepository members, NameRandomizer nameRandomizer) {
        this.members = members;
        this.nameRandomizer = nameRandomizer;
    }

    public void validate(Name name) {
        if (members.existsByName(name)) {
            throw new DuplicateNameException();
        }
    }

    public Name ofGuest() {
        Name name = nameRandomizer.getBy("guest");
        while (members.existsByName(name)) {
            name = nameRandomizer.getBy("guest");
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
