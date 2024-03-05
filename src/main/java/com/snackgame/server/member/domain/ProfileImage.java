package com.snackgame.server.member.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.snackgame.server.member.exception.InvalidProfileImageException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ProfileImage {

    private static final List<String> ALLOWED_SCHEMES = List.of("http", "https");

    public static final ProfileImage EMPTY = new ProfileImage(
            "https://snackgame.s3.ap-northeast-2.amazonaws.com/static/logo.png");

    @Column(name = "profile_image", nullable = false)
    private String url;

    public ProfileImage(String url) {
        validate(url);
        this.url = url;
    }

    private void validate(String url) {
        if (!ALLOWED_SCHEMES.contains(getSchemeOf(url))) {
            throw new InvalidProfileImageException();
        }
    }

    private String getSchemeOf(String url) {
        return url.split("://")[0];
    }

    public String getUrl() {
        return url;
    }
}
