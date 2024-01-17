package com.snackgame.server.member.domain;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.snackgame.server.auth.jwt.support.MemberResolver;

@Component
public class SnackgameMemberResolver implements MemberResolver<Member> {

    private final MemberRepository members;

    public SnackgameMemberResolver(MemberRepository members) {
        this.members = members;
    }

    @Override
    public Optional<Member> resolve(Long memberId) {
        return members.findById(memberId);
    }
}
