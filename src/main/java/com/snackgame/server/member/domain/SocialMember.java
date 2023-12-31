package com.snackgame.server.member.domain;

import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMember extends Member {

    private String email;
    private String nickname;
    private String picture;

    private String provider;
    private String providedId;

    public SocialMember(Name name, String provider, String providedId) {
        super(name);
        this.provider = provider;
        this.providedId = providedId;
    }

    private SocialMember(Long id, Name name, Group group, String provider, String providedId) {
        super(id, name, group);
        this.provider = provider;
        this.providedId = providedId;
    }

    public static SocialMember from(Member member, String provider, String providedId) {
        return new SocialMember(
                member.getId(), member.getName(), member.getGroup(),
                provider,
                providedId
        );
    }

    public String getProvider() {
        return provider;
    }

    public String getProvidedId() {
        return providedId;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.SOCIAL;
    }

    public void setAdditional(String email, String nickname, String picture) {
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
    }
}
