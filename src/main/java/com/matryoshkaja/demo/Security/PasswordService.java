package com.matryoshkaja.demo.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public String hash(String password){
        return passwordEncoder.encode(password);
    }

    public boolean matches(String rawPassword, String hashedPassword ){
        return passwordEncoder.matches(rawPassword,hashedPassword);
    }
}
