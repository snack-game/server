package com.snackgame.server.messaging.push.service;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.snackgame.server.messaging.push.service.dto.DeviceResponse;
import com.snackgame.server.messaging.push.service.dto.NotificationRequest;

@Service
public class FCMPushService implements PushService {

    private final DeviceService deviceService;

    public FCMPushService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public Future<?> sendPushMessage(NotificationRequest request, Long ownerId) {
        MulticastMessage multicastMessage = makeMessage(request, ownerId);
        return FirebaseMessaging.getInstance().sendEachForMulticastAsync(multicastMessage);
    }

    private MulticastMessage makeMessage(NotificationRequest request, Long ownerId) {

        List<DeviceResponse> devicesOf = deviceService.getDevicesOf(ownerId);

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(devicesOf.stream().map(DeviceResponse::getToken).collect(Collectors.toList()))
                .setNotification(request.toNotification())
                .build();
        return message;
    }

}
