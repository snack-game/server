package com.snackgame.server.messaging.channel

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Profile("dev")
@Component
class DevBusinessChannel(
    webClient: WebClient,
    @Value("\${discord.dev-business-channel-webhook}") webHookUrl: String
) : BusinessChannel(webClient, webHookUrl)
