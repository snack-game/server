package com.snackgame.server.messaging.push.service;

import java.util.concurrent.Future;

import com.snackgame.server.messaging.push.service.dto.NotificationRequest;

public interface PushService {

    Future<?> sendPushMessage(NotificationRequest request, Long ownerId);
}
