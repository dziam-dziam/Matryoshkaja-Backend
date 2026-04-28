package com.matryoshkaja.demo.Security;

import com.matryoshkaja.demo.Entities.Admin;
import com.matryoshkaja.demo.Enums.Role;
import com.matryoshkaja.demo.Repositories.AdminRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private Admin createAdmin(String email) {
        return Admin.builder()
                .id(1L)
                .email(email)
                .hashedPassword("hashed")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    @DisplayName("should return UserDetails when user exists")
    void shouldReturnUserDetailsWhenUserExists() {
        // given
        String email = "test@gmail.com";
        Admin admin = createAdmin(email);

        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(admin));

        // when
        UserDetails result = userDetailsService.loadUserByUsername(email);

        // then
        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertEquals("hashed", result.getPassword());

        verify(adminRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("should throw UsernameNotFoundException when user does not exist")
    void shouldThrowWhenUserNotFound() {
        // given
        String email = "notfound@gmail.com";

        when(adminRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email)
        );

        assertTrue(exception.getMessage().contains(email));

        verify(adminRepository, times(1)).findByEmail(email);
    }

}