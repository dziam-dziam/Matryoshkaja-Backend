package com.matryoshkaja.demo.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<? extends Admin> thisClass = Hibernate.getClass(this);
        Class<?> otherClass = Hibernate.getClass(object);
        if (thisClass != otherClass) return false;
        Admin other = (Admin) object;
        return this.id != null && this.id.equals(other.id);
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }

}
