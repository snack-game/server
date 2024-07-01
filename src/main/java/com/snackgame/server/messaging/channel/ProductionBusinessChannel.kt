package com.snackgame.server.messaging.channel

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Profile("production")
@Component
class ProductionBusinessChannel(
    webClient: WebClient,
    @Value("\${discord.business-channel-webhook}") webHookUrl: String
) : BusinessChannel(webClient, webHookUrl)
