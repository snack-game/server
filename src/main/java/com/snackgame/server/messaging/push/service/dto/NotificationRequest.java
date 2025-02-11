package com.snackgame.server.messaging.push.service.dto;

import com.google.firebase.messaging.Notification;

public class NotificationRequest {

    public String title;
    public String body;

    public NotificationRequest(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public Notification toNotification() {
        return Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
    }

}
