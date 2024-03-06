package com.snackgame.server.notification

import com.snackgame.server.common.exception.event.ExceptionalEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Profile("production")
@Component
open class ExceptionalEventListener(
    @Value("\${spring.profiles.active}")
    private val environment: String,
    @Value("\${discord.system-webhook-url}")
    private val systemWebhookUrl: String,
    private val discordNotification: DiscordNotification
) {

    @Async
    @EventListener
    open fun onException(exceptionalEvent: ExceptionalEvent) {
        discordNotification.send(systemWebhookUrl,
            buildString {
                appendLine("🚨 `$environment` 서버 예외 발생 🚨")
                append("`${exceptionalEvent.exceptionName}`: `${exceptionalEvent.message}`")
            }
        )
    }
}
