package com.snackgame.server.member.business.domain;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.snackgame.server.auth.jwt.MemberResolver;

@Component
public class MemberResolverImpl implements MemberResolver<Member> {

    private final MemberRepository members;

    public MemberResolverImpl(MemberRepository members) {
        this.members = members;
    }

    @Override
    public Optional<Member> resolve(Long memberId) {
        return members.findById(memberId);
    }
}
