package com.snackgame.server.game.sign.domain

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import java.security.PrivateKey

class Signer(private val privateKey: PrivateKey) {

    fun sign(content: String): String {
        runCatching { ObjectMapper().readTree(content) }
            .onFailure { throw Exception("Could not parse JSON") }

        return Jwts.builder()
            .header().keyId(privateKey.hashCode().toString())
            .and()
            .content(content)
            .signWith(privateKey)
            .compact()
    }
}
