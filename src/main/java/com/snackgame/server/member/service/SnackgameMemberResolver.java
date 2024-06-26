package com.snackgame.server.member.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.snackgame.server.auth.token.support.MemberResolver;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.MemberRepository;

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
