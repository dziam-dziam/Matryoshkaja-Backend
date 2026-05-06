package com.matryoshkaja.demo.Controllers;

import com.matryoshkaja.demo.Dtos.PageContentDto;
import com.matryoshkaja.demo.Services.PageContentServices.GetPageContentService;
import com.matryoshkaja.demo.Services.PageContentServices.UpdatePageContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/page-content")
@RequiredArgsConstructor
public class PageContentController {
    private final GetPageContentService getPageContentService;
    private final UpdatePageContentService updatePageContentService;

    @GetMapping
    public List<PageContentDto> getAll() {
        return getPageContentService.getAll();
    }

    // Prywatne: zapis działa tylko po JWT admina.
    @PutMapping
    public List<PageContentDto> updateAll(@RequestBody List<PageContentDto> request) {
        return updatePageContentService.updateAll(request);
    }
}
