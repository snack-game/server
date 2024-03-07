package com.snackgame.server.notification

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.Duration

private const val TIMEOUT = 2L

@Component
class DiscordNotification(
    private val webClient: WebClient
) {

    fun send(webHookUrl: String, message: String) {
        webClient.post()
            .uri(webHookUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(DiscordMessage(message))
            .retrieve()
            .bodyToMono<String>()
            .block(Duration.ofSeconds(TIMEOUT))
    }
}
