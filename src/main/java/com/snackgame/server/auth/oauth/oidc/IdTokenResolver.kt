package com.snackgame.server.auth.oauth.oidc

import com.snackgame.server.auth.exception.TokenInvalidException
import com.snackgame.server.auth.oauth.oidc.jwk.IdToken
import com.snackgame.server.auth.oauth.oidc.jwk.Issuer
import com.snackgame.server.auth.oauth.oidc.payload.IdTokenPayload
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class IdTokenResolver {
    fun resolve(rawIdToken: String): IdTokenPayload {
        val idToken = IdToken(rawIdToken)
        val issuer: Issuer = Issuer.of(idToken)
            ?: throw TokenInvalidException().also {
                log.warn("지원하지 않는 Id Token이 있었습니다: {}", rawIdToken)
            }
        return issuer.resolve(idToken)
    }

    companion object {
        private val log = LoggerFactory.getLogger(IdTokenResolver::class.java)
    }
}
