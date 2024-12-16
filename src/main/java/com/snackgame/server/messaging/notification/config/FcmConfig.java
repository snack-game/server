package com.snackgame.server.messaging.notification.config;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.snackgame.server.messaging.notification.exception.FcmException;

@Configuration
public class FcmConfig {

    @Value("${fcm.file_path}")
    private String firebaseKeyPath;

    @PostConstruct
    public void init() {
        try {
            InputStream serviceAccount = new ClassPathResource(firebaseKeyPath).getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException exception) {
            throw new FcmException();
        }
    }

}
