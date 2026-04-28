package com.matryoshkaja.demo.Security;

import com.matryoshkaja.demo.Entities.Admin;
import com.matryoshkaja.demo.Repositories.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataSeeder dataSeeder;

    @Test
    void shouldSeedWhenDatabaseEmpty() throws Exception {
        when(adminRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(any())).thenReturn("hashed");

        dataSeeder.run();

        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    void shouldNotSeedWhenDatabaseNotEmpty() throws Exception {
        when(adminRepository.count()).thenReturn(1L);

        dataSeeder.run();

        verify(adminRepository, never()).save(any());
    }

}