package com.example.insurance;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;

import jakarta.annotation.PostConstruct;
import lombok.Getter;

@Configuration
@Getter
public class FirebaseConfig {

    @Value("${firebase.bucket}")
    private String bucketName;

    @Value("${firebase.service-account}")
    private String serviceAccountPath;

    private Firestore firestore;

    @PostConstruct
    public void init() throws Exception {

        if (FirebaseApp.getApps().isEmpty()) {

            InputStream serviceAccount =
                    getClass().getClassLoader()
                            .getResourceAsStream(
                                    serviceAccountPath.replace("classpath:", "")
                            );

            if (serviceAccount == null) {
                throw new RuntimeException(
                        "Firebase service account file not found: " + serviceAccountPath
                );
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket(bucketName)
                    .build();

            FirebaseApp.initializeApp(options);
        }

        firestore = FirestoreClient.getFirestore();
    }

    // ✅ Correct way
    public Firestore getDb() {
        return firestore;
    }

    // ✅ Correct way (DO NOT use Storage)
    public StorageClient getStorageClient() {
        return StorageClient.getInstance();
    }
}
