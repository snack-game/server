package com.snackgame.server.messaging.push.service;

import java.util.concurrent.Future;

public interface PushService {

    public Future<?> sendPushMessage(String title, String body, Long ownerId);
}
