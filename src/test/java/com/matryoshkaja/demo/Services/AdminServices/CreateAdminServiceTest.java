package com.matryoshkaja.demo.Services.AdminServices;

import com.matryoshkaja.demo.Dtos.AdminDtos.AdminCreateDto;
import com.matryoshkaja.demo.Dtos.AdminDtos.AdminResponseDto;
import com.matryoshkaja.demo.Entities.Admin;
import com.matryoshkaja.demo.Enums.Role;
import com.matryoshkaja.demo.Exceptions.EmailAlreadyExistsException;
import com.matryoshkaja.demo.Mappers.AdminMapper;
import com.matryoshkaja.demo.Repositories.AdminRepository;
import com.matryoshkaja.demo.Security.PasswordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAdminServiceTest {

    @Mock
    AdminRepository adminRepository;

    @Mock
    AdminMapper adminMapper;

    @Mock
    PasswordService passwordService;

    @InjectMocks
    CreateAdminService createAdminService;

    @Test
    void shouldCreateNewAdmin(){
        //given
        AdminCreateDto adminCreateDto = AdminCreateDto.builder()
                .email("test@gmail.com")
                .password("Jarek123!")
                .build();

        Admin mappedAdmin = Admin.builder()
                .id(1L)
                .email("test@gmail.com")
                .build();

        Admin savedAdmin = Admin.builder()
                .id(1L)
                .email("test@gmail.com")
                .hashedPassword("hashed")
                .build();

        AdminResponseDto adminResponseDto = AdminResponseDto.builder()
                .id(1L)
                .email("test@gmail.com")
                .build();

        when(adminRepository.existsByEmail(anyString())).thenReturn(false);
        when(adminMapper.mapCreateDtoToEntity(adminCreateDto)).thenReturn(mappedAdmin);
        when(passwordService.hash(adminCreateDto.getPassword())).thenReturn("hashed");
        when(adminRepository.save(mappedAdmin)).thenReturn(savedAdmin);
        when(adminMapper.mapEntityToResponseDto(savedAdmin)).thenReturn(adminResponseDto);
        //when
        AdminResponseDto result = createAdminService.createAdmin(adminCreateDto);
        //then
        assertNotNull(result);
        assertEquals(Role.ADMIN, mappedAdmin.getRole());
        assertEquals(adminResponseDto.getId(),result.getId());
        assertEquals(adminResponseDto.getEmail(),result.getEmail());

        verify(adminMapper).mapCreateDtoToEntity(adminCreateDto);
        verify(passwordService).hash(adminCreateDto.getPassword());
        verify(adminRepository).save(mappedAdmin);
        verify(adminMapper).mapEntityToResponseDto(savedAdmin);
    }

    @Test
    void shouldNotCreateEntityWhenDtoIsNull(){
        //given null / when / then
        assertThrows(IllegalArgumentException.class, () -> createAdminService.createAdmin(null));
    }

    @Test
    void entityShouldHaveHashedPassword(){
        //given
        AdminCreateDto dto = AdminCreateDto.builder()
                .email("test@gmail.com")
                .password("Jacek12!")
                .build();

        Admin mappedAdmin = Admin.builder()
                .id(1L)
                .email("Jacek12!")
                .build();

        Admin saved = Admin.builder()
                .id(1L)
                .email("Jacek12!")
                .hashedPassword("hashed")
                .build();

        AdminResponseDto responseDto = AdminResponseDto.builder()
                .id(1L)
                .email("Jacek12!")
                .build();

        when(adminRepository.existsByEmail(anyString())).thenReturn(false);
        when(adminMapper.mapCreateDtoToEntity(dto)).thenReturn(mappedAdmin);
        when(passwordService.hash(dto.getPassword())).thenReturn("hashed");
        when(adminRepository.save(mappedAdmin)).thenReturn(saved);
        when(adminMapper.mapEntityToResponseDto(saved))
                .thenReturn(responseDto);
        //when

        createAdminService.createAdmin(dto);

        //then
        assertEquals("hashed", mappedAdmin.getHashedPassword());
    }

    @Test
    void shouldNotSaveWhenMapperFails(){
        //given
        AdminCreateDto dto = AdminCreateDto.builder()
                .email("test@gmail.com")
                .password("Jacek12!")
                .build();

        when(adminMapper.mapCreateDtoToEntity(dto)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> createAdminService.createAdmin(dto));

        verify(adminRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        // given
        AdminCreateDto dto = AdminCreateDto.builder()
                .email("test@gmail.com")
                .password("Test123!")
                .build();

        when(adminRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        // when + then
        assertThrows(EmailAlreadyExistsException.class,
                () -> createAdminService.createAdmin(dto));

        verify(adminRepository, never()).save(any());
        verify(passwordService, never()).hash(any());
        verify(adminMapper, never()).mapCreateDtoToEntity(any(AdminCreateDto.class));
    }

}