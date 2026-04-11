package com.matryoshkaja.demo.Repositories;

import com.matryoshkaja.demo.Entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Long, Photo> {
}
