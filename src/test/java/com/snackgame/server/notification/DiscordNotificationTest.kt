package com.snackgame.server.notification

import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.AssertionsForClassTypes.assertThatNoException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores::class)
@ServiceTest
class DiscordNotificationTest(
    @Value("\${discord.business-webhook-url}") private val webhookUrl: String,
    @Autowired private val discordNotification: DiscordNotification
) {

    @Test
    fun `디스코드로 메시지를 보낸다`() {
        assertThatNoException().isThrownBy { discordNotification.send(webhookUrl, "test") }
    }
}
