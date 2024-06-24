package com.tsp.mediaService.repositories;

import com.tsp.mediaService.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media,Long> {
    /**
     *
     * @param emailId
     * @return
     */
    Optional<Media> findByEmailId(String emailId);
}
