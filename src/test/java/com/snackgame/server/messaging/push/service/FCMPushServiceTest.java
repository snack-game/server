package com.snackgame.server.messaging.push.service;

import static com.snackgame.server.member.fixture.MemberFixture.정환;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.member.fixture.MemberFixture;
import com.snackgame.server.messaging.push.service.dto.DeviceTokenRequest;
import com.snackgame.server.support.general.ServiceTest;

@Disabled
@ServiceTest
public class FCMPushServiceTest {

    @Autowired
    private FCMPushService fcmPushService;

    @Autowired
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        MemberFixture.saveAll();
    }

    @DisplayName("토큰을 통해 푸시알림을 전송할 수 있다")
    @Test
    void sendMessage() throws ExecutionException, InterruptedException {
        deviceService.registerDeviceFor(정환().getId(),
                new DeviceTokenRequest(
                        "cFwP3VYHh0eyoXkyY9MwJr:APA91bHX5YiVXuIvi-pLDqNHcJMhl7hKrqLTC7opFMbzj4CsXrg1wu2ayG_LFVREto678gQdWGUnmBXwKEpEJTfXheX0Fz83xwqDzVrKvXF3H5t07XXU6e-boq8JnZVCbs6NB_VfGRh8"));

        Future<?> future = fcmPushService.sendPushMessage("테스트", "테스트", 정환().getId());

        future.get();
    }

}
