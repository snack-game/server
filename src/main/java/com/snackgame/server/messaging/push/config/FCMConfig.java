package com.snackgame.server.messaging.push.config;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.snackgame.server.messaging.push.exception.FCMException;

@Configuration
public class FCMConfig {

    private static final String FIREBASE_PATH_RESOURCE = "secrets/firebase-service-key.json";
    private static final String PROJECT_ID = "snackgame";

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount = new ClassPathResource(FIREBASE_PATH_RESOURCE).getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(PROJECT_ID)
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception exception) {
            throw new FCMException();
        }
    }

}
