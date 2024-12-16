package com.snackgame.server.messaging.notification.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.snackgame.server.messaging.notification.domain.FcmMessage;
import com.snackgame.server.messaging.notification.exception.FcmException;
import com.snackgame.server.messaging.notification.service.dto.DeviceResponse;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Service
public class FcmService {

    private static final String TOKEN_PREFIX = "Bearer ";
    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    @Value("${fcm.url}")
    private String apiURL;
    @Value("${fcm.file_path}")
    private String firebaseKeyPath;
    @Value("${fcm.google_api}")
    private String googleURLApi;

    public FcmService(ObjectMapper objectMapper, NotificationService notificationService) {
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    public void sendMessageTo(String title, String body, Long ownerId) {
        try {
            String message = makeMessage(title, body, ownerId);

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
            Request request = new Request.Builder()
                    .url(apiURL)
                    .post(requestBody)
                    .addHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + getAccessToken())
                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                    .build();

            client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String makeMessage(String title, String body, Long ownerId) {

        List<DeviceResponse> devicesOf = notificationService.getDevicesOf(ownerId);
        String token = devicesOf.stream().findFirst().get().getToken();
        try {
            FcmMessage message = FcmMessage.of(title, body, token);

            return objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getAccessToken() {

        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseKeyPath).getInputStream())
                    .createScoped(List.of(googleURLApi));
            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException exception) {
            throw new FcmException();
        }
    }

}
