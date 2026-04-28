package com.matryoshkaja.demo.Repositories;

import com.matryoshkaja.demo.Entities.Admin;
import com.matryoshkaja.demo.Enums.Role;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("local")
class AdminRepositoryTest {

    @Autowired
    AdminRepository adminRepository;

    private Admin createAdmin(String email) {
        return Admin.builder()
                .email(email)
                .hashedPassword("hashed")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    @DisplayName("should return true when email exists")
    void shouldReturnTrueWhenEmailExists(){
        //given
        Admin admin = createAdmin("test@gmail.com");
        adminRepository.save(admin);

        //when
        boolean exists = adminRepository.existsByEmail("test@gmail.com");

        //then
        assertTrue(exists);
    }

    @Test
    @DisplayName("should return false when email does not exists")
    void shouldReturnFalseWhenEmailDoesNotExist(){
        //given null & when
        boolean exists = adminRepository.existsByEmail("test@gmail.com");

        //then

        assertFalse(exists);
    }

    @Test
    @DisplayName("should save admin correctly")
    void shouldSaveAdminCorrectly(){
        //given
        Admin admin = createAdmin("test@gmail.com");

        //when
        Admin result = adminRepository.save(admin);

        //then
        assertNotNull(result.getId());
        assertEquals(admin.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("should find admin by id")
    void shouldFindAdminById(){
        //given
        Admin admin = createAdmin("test@gmail.com");
        Admin saved = adminRepository.save(admin);

        //when
        Optional<Admin> result = adminRepository.findById(saved.getId());

        //then
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(saved.getEmail(), result.get().getEmail());
    }

    @Test
    @DisplayName("should return empty when admin not found by id")
    void shouldReturnEmptyWhenAdminNotFoundById(){
        //given null & when
        Optional<Admin> result = adminRepository.findById(1L);

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should throw when saving duplicate")
    void shouldThrowWhenSavingDuplicate(){
        //given
        Admin admin1 = createAdmin("test@gmail.com");
        Admin admin2 = createAdmin("test@gmail.com");

        adminRepository.save(admin1);

        //when & then
        assertThrows(DataIntegrityViolationException.class,
                () -> adminRepository.saveAndFlush(admin2));

    }

    @Test
    @DisplayName("should delete admin")
    void shouldDeleteAdmin() {
        // given
        Admin saved = adminRepository.save(createAdmin("delete@test.com"));

        // when
        adminRepository.deleteById(saved.getId());

        // then
        boolean exists = adminRepository.existsByEmail("delete@test.com");
        assertFalse(exists);
    }

    @Test
    @DisplayName("should return false when email differs by case sensitivity")
    void shouldReturnFalseWhenChecksIfEmailWithDifferentCaseExists(){
        //given
        adminRepository.save(createAdmin("test@gmail.com"));

        //when
        boolean exists = adminRepository.existsByEmail("TEST@gmail.com");

        //then
        assertFalse(exists);
    }

    @Test
    @DisplayName("should update email for existing admin")
    void shouldUpdateExistingAdmin(){
        //given
        Admin saved = adminRepository.save(createAdmin("test@gmail.com"));

        //when
        saved.setEmail("new@gmail.com");

        //then
        assertEquals("new@gmail.com", saved.getEmail());
    }

    @Test
    @DisplayName("should delete admin by id")
    void shouldDeleteAdminById() {
        // given
        Admin saved = adminRepository.save(createAdmin("delete@test.com"));

        // when
        adminRepository.deleteById(saved.getId());

        // then
        Optional<Admin> result = adminRepository.findById(saved.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should handle null email")
    void shouldHandleNullEmail() {
        // given
        Admin admin = createAdmin(null);

        // when & then
        assertThrows(ConstraintViolationException.class,
                () -> adminRepository.saveAndFlush(admin));
    }

}