package com.snackgame.server.auth.oauth.attributes;

import java.util.Map;
import java.util.Objects;

import com.snackgame.server.auth.exception.OAuthAuthenticationException;

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
        return toStringRequired(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        return toStringOptional(kakaoAccount.get("email"));
    }

    @Override
    public String getName() {
        return toStringRequired(kakaoAccount.getOrDefault("name", getNickname()));
    }

    @Override
    public String getNickname() {
        return toStringOptional(profile.get("nickname"));
    }

    @Override
    public String getPictureUrl() {
        return toStringOptional(profile.get("profile_image_url"));
    }

    private String toStringOptional(Object object) {
        if (Objects.isNull(object)) {
            return null;
        }
        return object.toString();
    }

    private String toStringRequired(Object object) {
        if (Objects.isNull(object)) {
            throw new OAuthAuthenticationException("사용자가 제공하지 않은 항목이 있습니다");
        }
        return object.toString();
    }
}
