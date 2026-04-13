package com.matryoshkaja.demo.Services.AdminServices;

import com.matryoshkaja.demo.Dtos.AdminDtos.AdminResponseDto;
import com.matryoshkaja.demo.Entities.Admin;
import com.matryoshkaja.demo.Exceptions.AdminNotFoundException;
import com.matryoshkaja.demo.Mappers.AdminMapper;
import com.matryoshkaja.demo.Repositories.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAdminServicesTest {

    @Mock
    private AdminMapper adminMapper;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private GetAdminServices getAdminServices;

    @Test
    void shouldGetAdminById(){
        //given
        Long adminId = 1L;
        Admin admin = Admin.builder()
                .id(adminId)
                .email("test@mail.com")
                .hashedPassword("hashed")
                .build();

        AdminResponseDto responseDto = AdminResponseDto.builder()
                .id(adminId)
                .email("test@mail.com")
                .build();

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(adminMapper.mapEntityToResponseDto(admin)).thenReturn(responseDto);

        //when
        AdminResponseDto result = getAdminServices.getAdmin(adminId);

        //then
        assertNotNull(result);
        verify(adminMapper).mapEntityToResponseDto(admin);
        verify(adminRepository).findById(adminId);
        assertEquals(1L, result.getId());
        assertEquals("test@mail.com",result.getEmail());
    }

    @Test
    void shouldThrowWhenIdIsNull(){
        //given null / when / then
        assertThrows(IllegalArgumentException.class,
                () -> getAdminServices.getAdmin(null));
    }

    @Test
    void shouldThrowWhenAdminNotFound(){
        //given
        Long adminId = 1L;

        when(adminRepository.findById(adminId))
                .thenReturn(Optional.empty());

        //when / then
        assertThrows(AdminNotFoundException.class,
                () -> getAdminServices.getAdmin(adminId));
        verify(adminMapper, never()).mapEntityToResponseDto(any(Admin.class));
        verify(adminRepository).findById(adminId);
    }

    @Test
    void shouldGetAllAdmins(){
        //given
        List<Admin> admins = List.of(
                Admin.builder()
                        .id(1L)
                        .hashedPassword("hashed1")
                        .email("test1@gmail.com")
                        .build(),
                Admin.builder()
                        .id(2L)
                        .hashedPassword("hashed2")
                        .email("test2@gmail.com").build()
        );
        List<AdminResponseDto> responseDtos = List.of(
                AdminResponseDto.builder()
                        .id(1L)
                        .email("test1@gmail.com")
                        .build(),
                AdminResponseDto.builder()
                        .id(2L)
                        .email("test2@gmail.com").build()
        );
        when(adminRepository.findAll()).thenReturn(admins);
        when(adminMapper.mapEntityToResponseDto(admins.getFirst())).thenReturn(responseDtos.getFirst());
        when(adminMapper.mapEntityToResponseDto(admins.getLast())).thenReturn(responseDtos.getLast());

        //when
        List<AdminResponseDto> result = getAdminServices.getAllAdmins();

        //then
        assertNotNull(result);
        verify(adminRepository).findAll();
        verify(adminMapper).mapEntityToResponseDto(admins.getFirst());
        verify(adminMapper).mapEntityToResponseDto(admins.getLast());
        assertEquals(responseDtos,result);
        assertEquals(responseDtos.getFirst().getId(), result.getFirst().getId());
        assertEquals(responseDtos.getFirst().getEmail(), result.getFirst().getEmail());
        assertEquals(responseDtos.getLast().getId(), result.getLast().getId());
        assertEquals(responseDtos.getLast().getEmail(), result.getLast().getEmail());
    }

    @Test
    void shouldReturnEmptyWhenNoAdminsExist(){
        //given
        when(adminRepository.findAll()).thenReturn(List.of());

        //when
        List<AdminResponseDto> result = getAdminServices.getAllAdmins();

        //then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminRepository).findAll();
        verify(adminMapper, never()).mapEntityToResponseDto(any(Admin.class));
    }

}