package com.matryoshkaja.demo.Storage;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StorageService {

    public String generateKey(String originalFileName){
        String cleanName = originalFileName.replaceAll("\\s+", "_");
        return UUID.randomUUID() + "_" + cleanName;
    }
}
