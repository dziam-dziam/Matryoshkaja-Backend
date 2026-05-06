package com.matryoshkaja.demo.Mappers;

import com.matryoshkaja.demo.Dtos.PageContentDto;
import com.matryoshkaja.demo.Entities.PageContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
@AllArgsConstructor
public class PageContentMapper {

    public PageContentDto mapEntityToDto(PageContent content) {
        return PageContentDto.builder()
                .key(content.getKey())
                .value(content.getValue())
                .build();
    }

    public PageContent mapDtoToEntity(PageContentDto pageContentDto){
        return PageContent.builder()
                .key(pageContentDto.getKey())
                .value(pageContentDto.getValue())
                .build();
    }
}
