package com.snackgame.server.auth.oauth.oidc.jwk;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.snackgame.server.auth.exception.JwkRenewalFailureException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpStreamUtil {

    public static InputStream open(URL url) {
        try {
            return url.openStream();
        } catch (IOException e) {
            log.error("[{}] URL을 여는데 실패했습니다", HttpStreamUtil.class.getSimpleName(), e);
            throw new JwkRenewalFailureException("JWK 갱신에 실패했습니다");
        }
    }
}
