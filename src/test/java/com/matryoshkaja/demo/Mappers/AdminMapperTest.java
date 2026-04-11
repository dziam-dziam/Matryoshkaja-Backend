package com.matryoshkaja.demo.Mappers;

import com.matryoshkaja.demo.Dtos.AdminDtos.AdminCreateDto;
import com.matryoshkaja.demo.Dtos.AdminDtos.AdminResponseDto;
import com.matryoshkaja.demo.Entities.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminMapperTest {

    private AdminMapper adminMapper;

    @BeforeEach
    void setUp(){
        adminMapper = new AdminMapper();
    }

    @Test
    void shouldMapCreateDtoToEntity(){
        //given
        AdminCreateDto dto = AdminCreateDto.builder()
                .email("email@test.com")
                .password("Password123!")
                .build();
        //when
        Admin entity = adminMapper.mapCreateDtoToEntity(dto);
        //then
        assertNotNull(entity);
        assertEquals(entity.getEmail(),dto.getEmail());
    }

    @Test
    void shouldNotMapCreateDtoToEntityWhenDtoIsNull(){
        //given null & when & then
        assertThrows(IllegalArgumentException.class, () -> {
            adminMapper.mapCreateDtoToEntity(null);
        });
    }

    @Test
    void shouldNotSetIdAndPassword(){
        //given
        AdminCreateDto dto = AdminCreateDto.builder()
                .email("email@test.com")
                .password("Password123!")
                .build();
        //when
        Admin admin = adminMapper.mapCreateDtoToEntity(dto);
        //then
        assertNull(admin.getId());
        assertNull(admin.getHashedPassword());
    }

    @Test
    void shouldCreateNewInstance(){
        //given
        AdminCreateDto dto = AdminCreateDto.builder()
                .email("test@test.com")
                .password("Test12!")
                .build();
        //when
        Admin admin = adminMapper.mapCreateDtoToEntity(dto);
        //then
        assertNotSame(dto,admin);
    }

    @Test
    void shouldMapEntityToResponseDto(){
        //given
        Admin entity = Admin.builder()
                .id(1L)
                .email("test@test.com")
                .build();
        //when
        AdminResponseDto dto = adminMapper.mapEntityToResponseDto(entity);
        //then
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getEmail(), dto.getEmail());
    }

    @Test
    void shouldNotMapEntityToAdminResponseDtoWhenEntityIsNull(){
        //given null & when & then
        assertThrows(IllegalArgumentException.class, () ->{
            adminMapper.mapEntityToResponseDto(null);
        });
    }

}