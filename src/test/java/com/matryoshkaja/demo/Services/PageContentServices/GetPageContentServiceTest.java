package com.matryoshkaja.demo.Services.PageContentServices;

import com.matryoshkaja.demo.Dtos.PageContentDto;
import com.matryoshkaja.demo.Entities.PageContent;
import com.matryoshkaja.demo.Mappers.PageContentMapper;
import com.matryoshkaja.demo.Repositories.PageContentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetPageContentServiceTest {

    @Mock
    private PageContentRepository pageContentRepository;

    @Mock
    private PageContentMapper pageContentMapper;

    @InjectMocks
    private GetPageContentService getPageContentService;

    @Test
    void shouldGetAllPageContent() {
        // given
        PageContent titleContent = PageContent.builder()
                .id(1L)
                .key("about.title")
                .value("Title")
                .build();

        PageContent descriptionContent = PageContent.builder()
                .id(2L)
                .key("about.description")
                .value("Description")
                .build();

        PageContentDto titleDto = PageContentDto.builder()
                .key("about.title")
                .value("Title")
                .build();

        PageContentDto descriptionDto = PageContentDto.builder()
                .key("about.description")
                .value("Description")
                .build();

        when(pageContentRepository.findAll()).thenReturn(List.of(titleContent, descriptionContent));
        when(pageContentMapper.mapEntityToDto(titleContent)).thenReturn(titleDto);
        when(pageContentMapper.mapEntityToDto(descriptionContent)).thenReturn(descriptionDto);

        // when
        List<PageContentDto> result = getPageContentService.getAll();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("about.title", result.getFirst().getKey());
        assertEquals("Title", result.getFirst().getValue());

        assertEquals("about.description", result.get(1).getKey());
        assertEquals("Description", result.get(1).getValue());

        verify(pageContentRepository).findAll();
        verify(pageContentMapper).mapEntityToDto(titleContent);
        verify(pageContentMapper).mapEntityToDto(descriptionContent);
    }

    @Test
    void shouldReturnEmptyListWhenNoPageContentExists() {
        // given
        when(pageContentRepository.findAll()).thenReturn(List.of());

        // when
        List<PageContentDto> result = getPageContentService.getAll();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(pageContentRepository).findAll();
        verifyNoInteractions(pageContentMapper);
    }
}
