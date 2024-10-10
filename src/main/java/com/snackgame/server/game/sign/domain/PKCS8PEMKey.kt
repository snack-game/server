package com.snackgame.server.game.sign.domain

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

class PKCS8PEMKey(
    private val rawPEM: String
) {

    fun toPrivateKey(): PrivateKey {
        val base64EncodedKey = rawPEM.replace("\n", "")
            .removePrefix("-----BEGIN PRIVATE KEY-----")
            .removeSuffix("-----END PRIVATE KEY-----")

        return Base64.getDecoder().decode(base64EncodedKey).let {
            val keyFactory = KeyFactory.getInstance("RSA")
            keyFactory.generatePrivate(PKCS8EncodedKeySpec(it))
        }
    }
}
