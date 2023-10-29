package com.snackgame.server.member.business.domain;

import org.springframework.stereotype.Component;

import com.snackgame.server.auth.oauth.attributes.OAuthAttributes;
import com.snackgame.server.auth.oauth.support.SocialMemberSaver;

@Component
public class SocialMemberSaverImpl implements SocialMemberSaver<SocialMember> {

    private final MemberRepository members;
    private final DistinctNaming distinctNaming;

    public SocialMemberSaverImpl(MemberRepository members, DistinctNaming distinctNaming) {
        this.members = members;
        this.distinctNaming = distinctNaming;
    }

    @Override
    public SocialMember saveMemberFrom(OAuthAttributes attributes) {
        SocialMember member = members.findByProviderAndProvidedId(attributes.getProvider(), attributes.getId())
                .orElseGet(() -> new SocialMember(
                        distinctNaming.from(new Name(attributes.getName())),
                        attributes.getProvider(),
                        attributes.getId()
                ));
        member.setAdditional(attributes.getEmail(), attributes.getNickname(), attributes.getPictureUrl());
        return members.save(member);
    }
}
