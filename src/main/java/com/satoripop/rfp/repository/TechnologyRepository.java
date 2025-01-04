package com.satoripop.rfp.repository;

import com.satoripop.rfp.domain.Technology;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Technology entity.
 *
 * When extending this class, extend TechnologyRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TechnologyRepository extends TechnologyRepositoryWithBagRelationships, JpaRepository<Technology, Long> {
    Page<Technology> findByNameContainingIgnoreCase(String name, Pageable pageable);

    default Optional<Technology> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Technology> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Technology> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
