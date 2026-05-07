package com.matryoshkaja.demo.Mappers;

import com.matryoshkaja.demo.Dtos.PageContentDto;
import com.matryoshkaja.demo.Entities.PageContent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageContentMapperTest {

    private final PageContentMapper pageContentMapper = new PageContentMapper();

    @Test
    void shouldMapEntityToDto() {
        // given
        PageContent content = PageContent.builder()
                .id(1L)
                .key("about.title")
                .value("Title")
                .build();

        // when
        PageContentDto result = pageContentMapper.mapEntityToDto(content);

        // then
        assertNotNull(result);
        assertEquals("about.title", result.getKey());
        assertEquals("Title", result.getValue());
    }

    @Test
    void shouldMapDtoToEntity() {
        // given
        PageContentDto dto = PageContentDto.builder()
                .key("about.description")
                .value("Description")
                .build();

        // when
        PageContent result = pageContentMapper.mapDtoToEntity(dto);

        // then
        assertNotNull(result);
        assertEquals("about.description", result.getKey());
        assertEquals("Description", result.getValue());
        assertNull(result.getId());
    }

    @Test
    void shouldMapEntityToDtoWithNullValues() {
        // given
        PageContent content = PageContent.builder()
                .id(1L)
                .key(null)
                .value(null)
                .build();

        // when
        PageContentDto result = pageContentMapper.mapEntityToDto(content);

        // then
        assertNotNull(result);
        assertNull(result.getKey());
        assertNull(result.getValue());
    }

    @Test
    void shouldMapDtoToEntityWithNullValues() {
        // given
        PageContentDto dto = PageContentDto.builder()
                .key(null)
                .value(null)
                .build();

        // when
        PageContent result = pageContentMapper.mapDtoToEntity(dto);

        // then
        assertNotNull(result);
        assertNull(result.getId());
        assertNull(result.getKey());
        assertNull(result.getValue());
    }
}
