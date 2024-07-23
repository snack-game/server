package com.snackgame.server.member.service;

import org.springframework.stereotype.Component;

import com.snackgame.server.auth.oauth.attributes.OAuthAttributes;
import com.snackgame.server.auth.oauth.support.SocialMemberSaver;
import com.snackgame.server.member.domain.MemberRepository;
import com.snackgame.server.member.domain.ProfileImage;
import com.snackgame.server.member.domain.SocialMember;

@Component
public class SnackgameSocialMemberSaver implements SocialMemberSaver<SocialMember> {

    private final MemberRepository members;
    private final DistinctNaming distinctNaming;
    private final SocialMemberNameRandomizer nameRandomizer;

    public SnackgameSocialMemberSaver(MemberRepository members, DistinctNaming distinctNaming,
            SocialMemberNameRandomizer nameRandomizer) {
        this.members = members;
        this.distinctNaming = distinctNaming;
        this.nameRandomizer = nameRandomizer;
    }

    @Override
    public SocialMember saveMemberFrom(OAuthAttributes attributes) {
        SocialMember member = members.findByProviderAndProvidedId(attributes.getProvider(), attributes.getId())
                .orElseGet(() -> new SocialMember(
                        distinctNaming.from(nameRandomizer.getName()),
                        new ProfileImage(attributes.getPictureUrl()),
                        attributes.getProvider(),
                        attributes.getId()
                ));
        member.setAdditional(attributes.getEmail(), attributes.getNickname());
        return members.save(member);
    }
}
