package com.snackgame.server.messaging

import com.snackgame.server.member.event.NewMemberEvent
import com.snackgame.server.messaging.channel.BusinessChannel
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Profile("production", "dev")
@Component
class BusinessEventListener(
    private val businessChannel: BusinessChannel
) {

    @Async
    @EventListener
    fun onNewMember(newMemberEvent: NewMemberEvent) {
        businessChannel.send(
            DiscordMessage(
                "새 사용자 가입: `${newMemberEvent.name}`"
            )
        )
    }
}
