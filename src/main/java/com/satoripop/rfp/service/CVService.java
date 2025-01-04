package com.satoripop.rfp.service;

import com.satoripop.rfp.service.dto.CVDTO;
import com.satoripop.rfp.service.dto.ContextDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.rfp.domain.CV}.
 */
public interface CVService {
    /**
     * Save a cV.
     *
     * @param cVDTO the entity to save.
     * @return the persisted entity.
     */
    CVDTO save(CVDTO cVDTO);

    /**
     * Updates a cV.
     *
     * @param cVDTO the entity to update.
     * @return the persisted entity.
     */
    CVDTO update(CVDTO cVDTO);

    /**
     * Partially updates a cV.
     *
     * @param cVDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CVDTO> partialUpdate(CVDTO cVDTO);

    /**
     * Get all the cVS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CVDTO> findAll(Pageable pageable);

    /**
     * Get all the cVS with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CVDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" cV.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CVDTO> findOne(Long id);

    /**
     * Delete the "id" cV.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    Page<CVDTO> searchByTitle(String title, Pageable pageable);
}
