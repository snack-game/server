package com.snackgame.server.messaging.push.service.dto;

import com.google.firebase.messaging.Notification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationRequest {

    public String title;
    public String body;

    public Notification toNotification() {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

}
