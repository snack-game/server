package com.snackgame.server.messaging.push.service.dto;

import com.google.firebase.messaging.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    public String title;
    public String body;

    public static Notification toNotificationWith(String title, String body) {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

}
