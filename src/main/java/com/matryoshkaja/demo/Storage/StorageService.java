package com.matryoshkaja.demo.Storage;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class StorageService {

    public String generateKey(String originalFileName){
        if(originalFileName == null || originalFileName.isBlank()) {
            originalFileName = "fileName.png";
        }
        String cleanName = originalFileName.replaceAll("\\s+", "_");
        return UUID.randomUUID() + "_" + cleanName;
    }

    public String upload(MultipartFile file, String key) {
        return "https://fake-storage.com/" + key;
    }

    public void delete(String key){

    }
}
