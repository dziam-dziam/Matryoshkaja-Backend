package com.matryoshkaja.demo.Security;

import com.matryoshkaja.demo.Entities.Admin;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;
    private final String role;

    public CustomUserDetails(Admin admin){
        this.id = admin.getId();
        this.email = admin.getEmail();
        this.password = admin.getHashedPassword();
        this.role = admin.getRole().name();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + role);
    }

    @Override
    public String getUsername() {
        return email;
    }
}
