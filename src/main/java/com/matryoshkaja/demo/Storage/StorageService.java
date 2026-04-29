package com.matryoshkaja.demo.Storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final S3Client s3Client;

    @Value("${storage_bucket_name}")
    private String bucket;

    @Value("${public_url}")
    private String baseUrl;

    public String generateKey(String originalFileName){
        if(originalFileName == null || originalFileName.isBlank()) {
            originalFileName = "fileName.png";
        }
        String cleanName = originalFileName.replaceAll("\\s+", "_");
        return UUID.randomUUID() + "_" + cleanName;
    }

    public String upload(MultipartFile file, String key) {
        try{
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            return baseUrl + "/" + key;
        } catch (Exception exception){
            throw new RuntimeException("Upload failed", exception);
        }
    }

    public void delete(String key){
        try{
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build()
            );
        } catch (Exception exception){
            throw new RuntimeException("Delete failed", exception);
        }
    }
}
