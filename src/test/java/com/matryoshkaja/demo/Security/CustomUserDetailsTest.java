package com.matryoshkaja.demo.Security;

import com.matryoshkaja.demo.Entities.Admin;
import com.matryoshkaja.demo.Enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void shouldMapAdminCorrectly() {
        Admin admin = Admin.builder()
                .id(1L)
                .email("test@gmail.com")
                .hashedPassword("hashed")
                .role(Role.ADMIN)
                .build();

        CustomUserDetails user = new CustomUserDetails(admin);

        assertEquals(1L, user.getId());
        assertEquals("test@gmail.com", user.getUsername());
        assertEquals("hashed", user.getPassword());
    }

    @Test
    void shouldReturnCorrectAuthorities() {
        Admin admin = Admin.builder()
                .id(1L)
                .email("test@gmail.com")
                .hashedPassword("hashed")
                .role(Role.ADMIN)
                .build();

        CustomUserDetails user = new CustomUserDetails(admin);

        String authority = user.getAuthorities().iterator().next().getAuthority();

        assertEquals("ROLE_ADMIN", authority);
    }

}