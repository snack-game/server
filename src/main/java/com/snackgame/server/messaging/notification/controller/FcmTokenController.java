package com.snackgame.server.messaging.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.token.support.Authenticated;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.messaging.notification.service.FcmService;
import com.snackgame.server.messaging.notification.service.dto.NotificationDto;

@RestController
public class FcmTokenController {

    private final FcmService fcmService;

    public FcmTokenController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/notifications/send")
    public ResponseEntity<String> pushMessage(@Authenticated Member member,
            @RequestBody NotificationDto notificationDto) {

        fcmService.sendMessageTo(notificationDto.title, notificationDto.body, member.getId());
        return ResponseEntity.ok().build();
    }

}
