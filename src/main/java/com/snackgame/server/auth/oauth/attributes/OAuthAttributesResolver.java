package com.snackgame.server.auth.oauth.attributes;

import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributesResolver {

    GOOGLE(GoogleOAuthAttributes::new),
    KAKAO(KakaoOAuthAttributes::new);

    private final Function<Map<String, Object>, OAuthAttributes> attributesResolver;

    OAuthAttributesResolver(Function<Map<String, Object>, OAuthAttributes> attributesResolver) {
        this.attributesResolver = attributesResolver;
    }

    public OAuthAttributes resolve(Map<String, Object> attributes) {
        return attributesResolver.apply(attributes);
    }
}
