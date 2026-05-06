package com.matryoshkaja.demo.Services.PageContentServices;

import com.matryoshkaja.demo.Dtos.PageContentDto;
import com.matryoshkaja.demo.Entities.PageContent;
import com.matryoshkaja.demo.Repositories.PageContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatePageContentService {
    private final PageContentRepository pageContentRepository;
    private final GetPageContentService getPageContentService;


    public List<PageContentDto> updateAll(List<PageContentDto> request) {
        if (request == null) {
            throw new IllegalArgumentException("Content request cannot be null");
        }

        request.forEach(item -> {
            if (item.getKey() == null || item.getKey().isBlank()) {
                throw new IllegalArgumentException("Content key cannot be empty");
            }

            PageContent content = pageContentRepository.findByKey(item.getKey())
                    .orElseGet(() -> PageContent.builder().key(item.getKey()).build());

            content.setValue(item.getValue() == null ? "" : item.getValue());
            pageContentRepository.save(content);
        });

        return getPageContentService.getAll();
    }

}
