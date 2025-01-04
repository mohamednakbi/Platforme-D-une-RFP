package com.satoripop.rfp.repository;

import com.satoripop.rfp.domain.CV;
import com.satoripop.rfp.domain.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Document entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
