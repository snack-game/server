package com.snackgame.server.messaging.notification.service;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.snackgame.server.messaging.notification.service.dto.DeviceResponse;
import com.snackgame.server.messaging.notification.service.dto.NotificationDto;

@Service
public class FCMPushService implements PushService {

    private final NotificationService notificationService;

    public FCMPushService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public Future<?> sendPushMessage(String title, String body, Long ownerId) {
        MulticastMessage multicastMessage = makeMessage(title, body, ownerId);
        return FirebaseMessaging.getInstance().sendEachForMulticastAsync(multicastMessage);
    }

    private MulticastMessage makeMessage(String title, String body, Long ownerId) {

        List<DeviceResponse> devicesOf = notificationService.getDevicesOf(ownerId);

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(devicesOf.stream().map(DeviceResponse::getToken).collect(Collectors.toList()))
                .setNotification(NotificationDto.toNotificationWith(title, body))
                .build();
        return message;
    }

}
