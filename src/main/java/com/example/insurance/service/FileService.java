package com.example.insurance.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.insurance.FirebaseConfig;
import com.example.insurance.model.DocumentFile;
import com.google.cloud.storage.Bucket;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FirebaseConfig firebase;

    public DocumentFile upload(String customerId, MultipartFile file) throws Exception {

        String id = UUID.randomUUID().toString();
        String fileName =
                "customers/" + customerId + "/" + id + "-" + file.getOriginalFilename();

        // ✅ CORRECT way to get bucket
        Bucket bucket = firebase.getStorageClient().bucket();

        bucket.create(
                fileName,
                file.getBytes(),
                file.getContentType()
        );

        String url =
                "https://firebasestorage.googleapis.com/v0/b/"
                        + bucket.getName()
                        + "/o/"
                        + fileName.replace("/", "%2F")
                        + "?alt=media";

        return new DocumentFile(
                id,
                file.getOriginalFilename(),
                url,
                file.getContentType(),
                file.getSize()
        );
    }
}
