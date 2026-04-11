package com.matryoshkaja.demo.Mappers;

import com.matryoshkaja.demo.Dtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhotoMapperTest {

    private PhotoMapper photoMapper;

    @BeforeEach
    void setUp(){
        photoMapper = new PhotoMapper();
    }

    @Test
    void shouldMapEntityToResponseDto(){
        //given
        Photo entity = Photo.builder()
                .id(1L)
                .imageUrl("testUrl")
                .build();
        //when
        PhotoResponseDto dto = photoMapper.mapEntityToResponseDto(entity);

        //then
        assertNotNull(dto);
        assertEquals(entity.getId(),dto.getId());
        assertEquals(entity.getImageUrl(),dto.getImageUrl());
    }

    @Test
    void shouldNotMapEntityToResponseDtoWhenEntityIsNull(){
        //given null & when & then
        assertThrows(IllegalArgumentException.class,() ->
                photoMapper.mapEntityToResponseDto(null));
    }

    @Test
    void shouldMapResponseDtoToEntity(){
        //given
        PhotoResponseDto dto = PhotoResponseDto.builder()
                .id(1L)
                .imageUrl("testUrl")
                .build();
        //when
        Photo entity = photoMapper.mapResponseDtoToEntity(dto);

        //then
        assertNotNull(entity);
        assertEquals(dto.getId(),entity.getId());
        assertEquals(dto.getImageUrl(),entity.getImageUrl());
    }

    @Test
    void shouldNotMapResponseDtoToEntityWhenDtoIsNull(){
        //given null & when & then
        assertThrows(IllegalArgumentException.class,() ->
                photoMapper.mapResponseDtoToEntity(null));
    }

    @Test
    void shouldNotSetImageKeyWhenMappingFromDto() {
        PhotoResponseDto dto = PhotoResponseDto.builder()
                .id(1L)
                .imageUrl("url")
                .build();

        Photo entity = photoMapper.mapResponseDtoToEntity(dto);

        assertNull(entity.getImageKey());
    }

}