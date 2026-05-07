package com.matryoshkaja.demo.Services.PageContentServices;

import com.matryoshkaja.demo.Dtos.PageContentDto;
import com.matryoshkaja.demo.Entities.PageContent;
import com.matryoshkaja.demo.Repositories.PageContentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePageContentServiceTest {

    @Mock
    private PageContentRepository pageContentRepository;

    @Mock
    private GetPageContentService getPageContentService;

    @InjectMocks
    private UpdatePageContentService updatePageContentService;

    @Test
    void shouldUpdateExistingContent() {
        // given
        PageContentDto requestDto = PageContentDto.builder()
                .key("about.title")
                .value("New title")
                .build();

        PageContent existingContent = PageContent.builder()
                .id(1L)
                .key("about.title")
                .value("Old title")
                .build();

        List<PageContentDto> expectedResponse = List.of(
                PageContentDto.builder()
                        .key("about.title")
                        .value("New title")
                        .build()
        );

        when(pageContentRepository.findByKey("about.title")).thenReturn(Optional.of(existingContent));
        when(getPageContentService.getAll()).thenReturn(expectedResponse);

        // when
        List<PageContentDto> result = updatePageContentService.updateAll(List.of(requestDto));

        // then
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        assertEquals("New title", existingContent.getValue());

        verify(pageContentRepository).findByKey("about.title");
        verify(pageContentRepository).save(existingContent);
        verify(getPageContentService).getAll();
    }

    @Test
    void shouldCreateContentWhenKeyDoesNotExist() {
        // given
        PageContentDto requestDto = PageContentDto.builder()
                .key("about.description")
                .value("New description")
                .build();

        List<PageContentDto> expectedResponse = List.of(
                PageContentDto.builder()
                        .key("about.description")
                        .value("New description")
                        .build()
        );

        when(pageContentRepository.findByKey("about.description")).thenReturn(Optional.empty());
        when(getPageContentService.getAll()).thenReturn(expectedResponse);

        ArgumentCaptor<PageContent> contentCaptor = ArgumentCaptor.forClass(PageContent.class);

        // when
        List<PageContentDto> result = updatePageContentService.updateAll(List.of(requestDto));

        // then
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(pageContentRepository).findByKey("about.description");
        verify(pageContentRepository).save(contentCaptor.capture());
        verify(getPageContentService).getAll();

        PageContent savedContent = contentCaptor.getValue();
        assertEquals("about.description", savedContent.getKey());
        assertEquals("New description", savedContent.getValue());
    }

    @Test
    void shouldUpdateMultipleContentItems() {
        // given
        List<PageContentDto> request = List.of(
                PageContentDto.builder()
                        .key("about.title")
                        .value("Title")
                        .build(),
                PageContentDto.builder()
                        .key("about.description")
                        .value("Description")
                        .build()
        );

        PageContent titleContent = PageContent.builder()
                .id(1L)
                .key("about.title")
                .value("Old title")
                .build();

        PageContent descriptionContent = PageContent.builder()
                .id(2L)
                .key("about.description")
                .value("Old description")
                .build();

        List<PageContentDto> expectedResponse = List.of(
                PageContentDto.builder()
                        .key("about.title")
                        .value("Title")
                        .build(),
                PageContentDto.builder()
                        .key("about.description")
                        .value("Description")
                        .build()
        );

        when(pageContentRepository.findByKey("about.title")).thenReturn(Optional.of(titleContent));
        when(pageContentRepository.findByKey("about.description")).thenReturn(Optional.of(descriptionContent));
        when(getPageContentService.getAll()).thenReturn(expectedResponse);

        // when
        List<PageContentDto> result = updatePageContentService.updateAll(request);

        // then
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        assertEquals("Title", titleContent.getValue());
        assertEquals("Description", descriptionContent.getValue());

        verify(pageContentRepository).findByKey("about.title");
        verify(pageContentRepository).findByKey("about.description");
        verify(pageContentRepository).save(titleContent);
        verify(pageContentRepository).save(descriptionContent);
        verify(getPageContentService).getAll();
    }

    @Test
    void shouldSetEmptyValueWhenValueIsNull() {
        // given
        PageContentDto requestDto = PageContentDto.builder()
                .key("about.subtitle")
                .value(null)
                .build();

        PageContent existingContent = PageContent.builder()
                .id(1L)
                .key("about.subtitle")
                .value("Old subtitle")
                .build();

        List<PageContentDto> expectedResponse = List.of(
                PageContentDto.builder()
                        .key("about.subtitle")
                        .value("")
                        .build()
        );

        when(pageContentRepository.findByKey("about.subtitle")).thenReturn(Optional.of(existingContent));
        when(getPageContentService.getAll()).thenReturn(expectedResponse);

        // when
        List<PageContentDto> result = updatePageContentService.updateAll(List.of(requestDto));

        // then
        assertNotNull(result);
        assertEquals(expectedResponse, result);
        assertEquals("", existingContent.getValue());

        verify(pageContentRepository).findByKey("about.subtitle");
        verify(pageContentRepository).save(existingContent);
        verify(getPageContentService).getAll();
    }

    @Test
    void shouldReturnAllContentWhenRequestIsEmpty() {
        // given
        List<PageContentDto> expectedResponse = List.of(
                PageContentDto.builder()
                        .key("about.title")
                        .value("Title")
                        .build()
        );

        when(getPageContentService.getAll()).thenReturn(expectedResponse);

        // when
        List<PageContentDto> result = updatePageContentService.updateAll(List.of());

        // then
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verifyNoInteractions(pageContentRepository);
        verify(getPageContentService).getAll();
    }

    @Test
    void shouldThrowWhenRequestIsNull() {
        // given null / when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> updatePageContentService.updateAll(null));

        assertEquals("Content request cannot be null", exception.getMessage());

        verifyNoInteractions(pageContentRepository);
        verifyNoInteractions(getPageContentService);
    }

    @Test
    void shouldThrowWhenContentKeyIsNull() {
        // given
        PageContentDto requestDto = PageContentDto.builder()
                .key(null)
                .value("Value")
                .build();

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> updatePageContentService.updateAll(List.of(requestDto)));

        assertEquals("Content key cannot be empty", exception.getMessage());

        verifyNoInteractions(pageContentRepository);
        verifyNoInteractions(getPageContentService);
    }

    @Test
    void shouldThrowWhenContentKeyIsBlank() {
        // given
        PageContentDto requestDto = PageContentDto.builder()
                .key("    ")
                .value("Value")
                .build();

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> updatePageContentService.updateAll(List.of(requestDto)));

        assertEquals("Content key cannot be empty", exception.getMessage());

        verifyNoInteractions(pageContentRepository);
        verifyNoInteractions(getPageContentService);
    }
}
