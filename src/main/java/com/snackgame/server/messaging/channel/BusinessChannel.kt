package com.snackgame.server.messaging.channel

import org.springframework.web.reactive.function.client.WebClient

abstract class BusinessChannel(
    webClient: WebClient,
    webHookUrl: String,
) : DiscordChannel(webClient, webHookUrl)
