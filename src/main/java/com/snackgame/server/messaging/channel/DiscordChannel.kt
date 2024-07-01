package com.snackgame.server.messaging.channel

import com.snackgame.server.messaging.DiscordMessage
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.Duration

private const val TIMEOUT = 2L

abstract class DiscordChannel(
    private val webClient: WebClient,
    private val webHookUrl: String
) {

    fun send(message: DiscordMessage) {
        webClient.post()
            .uri(webHookUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(message)
            .retrieve()
            .bodyToMono<String>()
            .block(Duration.ofSeconds(TIMEOUT))
    }
}
