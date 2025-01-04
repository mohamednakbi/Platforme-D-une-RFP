package com.satoripop.rfp.repository;

import com.satoripop.rfp.domain.UserConfig;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserConfig entity.
 *
 * When extending this class, extend UserConfigRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface UserConfigRepository extends UserConfigRepositoryWithBagRelationships, JpaRepository<UserConfig, Long> {
    Page<UserConfig> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    default Optional<UserConfig> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<UserConfig> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<UserConfig> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
