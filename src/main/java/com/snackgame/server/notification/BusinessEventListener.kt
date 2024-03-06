package com.snackgame.server.notification

import com.snackgame.server.member.event.NewMemberEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
open class BusinessEventListener(
    @Value("\${discord.business-webhook-url}")
    private val businessWebhookUrl: String,
    @Value("\${discord.system-webhook-url}")
    private val systemWebhookUrl: String,
    private val discordNotification: DiscordNotification
) {

    @Async
    @EventListener
    open fun onNewMember(newMemberEvent: NewMemberEvent) {
        discordNotification.send(
            businessWebhookUrl,
            "새 사용자 ${newMemberEvent.name}가 가입했습니다"
        )
    }
}
