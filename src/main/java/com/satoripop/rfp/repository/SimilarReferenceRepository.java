package com.satoripop.rfp.repository;

import com.satoripop.rfp.domain.Reference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SimilarReference entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SimilarReferenceRepository extends JpaRepository<Reference, Long> {
    Page<Reference> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
