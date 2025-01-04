package com.satoripop.rfp.repository;

import com.satoripop.rfp.domain.CV;
import com.satoripop.rfp.domain.Context;
import com.satoripop.rfp.service.dto.CVDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CV entity.
 */
@Repository
public interface CVRepository extends JpaRepository<CV, Long> {
    Page<CV> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    default Optional<CV> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CV> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CV> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select cV from CV cV left join fetch cV.userConfig", countQuery = "select count(cV) from CV cV")
    Page<CV> findAllWithToOneRelationships(Pageable pageable);

    @Query("select cV from CV cV left join fetch cV.userConfig")
    List<CV> findAllWithToOneRelationships();

    @Query("select cV from CV cV left join fetch cV.userConfig where cV.id =:id")
    Optional<CV> findOneWithToOneRelationships(@Param("id") Long id);
}
