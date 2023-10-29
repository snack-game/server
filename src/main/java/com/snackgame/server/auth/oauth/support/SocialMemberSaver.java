package com.snackgame.server.auth.oauth.support;

import com.snackgame.server.auth.oauth.attributes.OAuthAttributes;

public interface SocialMemberSaver<T> {

    T saveMemberFrom(OAuthAttributes oAuthAttributes);
}
