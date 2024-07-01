package com.snackgame.server.messaging

import com.snackgame.server.common.exception.event.ExceptionalEvent
import com.snackgame.server.messaging.channel.ExceptionChannel
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Profile("production", "dev")
@Component
class ExceptionalEventListener(
    @Value("\${spring.profiles.active}") private val environment: String,
    private val exceptionChannel: ExceptionChannel
) {

    @Async
    @EventListener
    fun onException(exceptionalEvent: ExceptionalEvent) {
        exceptionChannel.send(DiscordMessage(buildString {
            appendLine("🚨 `$environment` 서버 예외 발생 🚨")
            append("`${exceptionalEvent.exceptionName}`: `${exceptionalEvent.message}`")
        }))
    }
}
