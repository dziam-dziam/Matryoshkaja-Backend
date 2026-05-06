package com.matryoshkaja.demo.Services.PageContentServices;

import com.matryoshkaja.demo.Dtos.PageContentDto;
import com.matryoshkaja.demo.Mappers.PageContentMapper;
import com.matryoshkaja.demo.Repositories.PageContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPageContentService {

    private final PageContentRepository pageContentRepository;
    private final PageContentMapper pageContentMapper;

    public List<PageContentDto> getAll() {
        return pageContentRepository.findAll()
                .stream()
                .map(pageContentMapper::mapEntityToDto)
                .toList();
    }
}
