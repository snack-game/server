package com.snackgame.server.messaging.notification.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class FcmMessage {

    private boolean validateOnly;
    private Message message;

    public static FcmMessage of(String title, String body, String token) {
        return FcmMessage.builder()
                .validateOnly(false)
                .message(Message.of(title, body, token))
                .build();
    }

    @Builder
    @AllArgsConstructor
    public static class Message {
        private Notification notification;
        private String token;

        private static Message of(String title, String body, String token) {
            return Message.builder()
                    .notification(Notification.of(title, body))
                    .token(token)
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    public static class Notification {
        private String title;
        private String body;
        private String image;

        private static Notification of(String title, String body) {
            return Notification.builder()
                    .title(title)
                    .body(body)
                    .build();
        }
    }

}
