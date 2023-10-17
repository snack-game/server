package com.snackgame.server.auth.oauth.attributes;

import java.util.Map;

public class KakaoOAuthAttributes implements OAuthAttributes {

    private static final String KAKAO_ACCOUNT_KEY = "kakao_account";
    private static final String KAKAO_PROFILE_KEY = "profile";

    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccount;
    private final Map<String, Object> profile;

    public KakaoOAuthAttributes(final Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccount = (Map<String, Object>)attributes.get(KAKAO_ACCOUNT_KEY);
        this.profile = (Map<String, Object>)kakaoAccount.get(KAKAO_PROFILE_KEY);
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getName() {
        return kakaoAccount.get("name").toString();
    }

    @Override
    public String getNickname() {
        return profile.get("nickname").toString();
    }

    @Override
    public String getPictureUrl() {
        return profile.get("profile_image_url").toString();
    }
}
