package com.snackgame.server.member.domain;

import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialMember extends Member {

    private String email;
    private String nickname;

    private String provider;
    private String providedId;

    public SocialMember(Name name, ProfileImage profileImage, String provider, String providedId) {
        super(name, profileImage);
        this.provider = provider;
        this.providedId = providedId;
    }

    public SocialMember(Long id, Name name, Group group, String provider, String providedId) {
        super(id, name, group);
        this.provider = provider;
        this.providedId = providedId;
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

    public void setAdditional(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
