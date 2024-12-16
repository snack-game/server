package com.snackgame.server.messaging.notification.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    public String title;
    public String body;

    public static NotificationDto of(String title, String body) {
        return NotificationDto.builder()
                .title(title)
                .body(body)
                .build();
    }

}
