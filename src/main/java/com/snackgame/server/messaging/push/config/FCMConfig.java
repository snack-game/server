package com.snackgame.server.messaging.push.config;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.snackgame.server.messaging.push.exception.FCMException;

@Configuration
public class FCMConfig {

    private static final String FIREBASE_PATH_RESOURCE = "firebase-service-key.json";
    @Value("${fcm.project_id}")
    private String projectId;

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount = new ClassPathResource(FIREBASE_PATH_RESOURCE).getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(projectId)
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception exception) {
            throw new FCMException();
        }
    }

}
