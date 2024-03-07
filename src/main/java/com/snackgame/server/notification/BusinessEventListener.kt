package com.snackgame.server.notification

import com.snackgame.server.member.event.NewMemberEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Profile("production")
@Component
open class BusinessEventListener(
    @Value("\${discord.business-webhook-url}")
    private val businessWebhookUrl: String,
    private val discordNotification: DiscordNotification
) {

    @Async
    @EventListener
    open fun onNewMember(newMemberEvent: NewMemberEvent) {
        discordNotification.send(
            businessWebhookUrl,
            "새 사용자 가입: `${newMemberEvent.name}`"
        )
    }
}
