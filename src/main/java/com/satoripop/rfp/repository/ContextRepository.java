package com.satoripop.rfp.repository;

import com.satoripop.rfp.domain.Context;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Context entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContextRepository extends JpaRepository<Context, Long> {
    Page<Context> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
