package com.matryoshkaja.demo.Repositories;

import com.matryoshkaja.demo.Entities.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    // REORDER CHANGE: public lookbook order.
    List<Photo> findAllByOrderByDisplayOrderAscIdAsc();

    // REORDER CHANGE: used when adding a new photo at the end.
    Optional<Photo> findTopByOrderByDisplayOrderDesc();
}
