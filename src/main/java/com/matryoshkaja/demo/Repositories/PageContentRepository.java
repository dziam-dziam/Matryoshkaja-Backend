package com.matryoshkaja.demo.Repositories;

import com.matryoshkaja.demo.Entities.PageContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PageContentRepository extends JpaRepository<PageContent, Long> {
    Optional<PageContent> findByKey(String key);
}
