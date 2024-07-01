package com.snackgame.server.messaging.channel

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class ExceptionChannel(
    webClient: WebClient,
    @Value("\${discord.exception-channel-webhook}") webHookUrl: String
) : DiscordChannel(webClient, webHookUrl)

