package com.snackgame.server.game.sign.config

import com.snackgame.server.game.sign.domain.PKCS8PEMKey
import com.snackgame.server.game.sign.domain.Signer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SignerConfig(
    @Value("\${game.signing.private-key-pem}")
    privateKeyPem: String
) {
    private val privateKey = PKCS8PEMKey(privateKeyPem).toPrivateKey()

    @Bean
    fun signer() = Signer(privateKey)
}
