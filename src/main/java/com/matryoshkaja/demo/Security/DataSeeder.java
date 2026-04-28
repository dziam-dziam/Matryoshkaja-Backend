package com.matryoshkaja.demo.Security;

import com.matryoshkaja.demo.Entities.Admin;
import com.matryoshkaja.demo.Enums.Role;
import com.matryoshkaja.demo.Repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AdminRepository  adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.count() == 0){
            Admin admin = new Admin();
            admin.setEmail("matryoshkaja@gmail.com");
            admin.setHashedPassword(passwordEncoder.encode("K@ja2002!!"));
            admin.setRole(Role.ADMIN);

            adminRepository.save(admin);
        }
    }
}
