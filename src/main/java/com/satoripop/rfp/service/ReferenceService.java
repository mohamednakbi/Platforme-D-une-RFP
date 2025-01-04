package com.satoripop.rfp.service;

import com.satoripop.rfp.service.dto.ReferenceDTO;
import com.satoripop.rfp.service.dto.UserConfigDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.rfp.domain.Reference}.
 */
public interface ReferenceService {
    /**
     * Save a reference.
     *
     * @param referenceDTO the entity to save.
     * @return the persisted entity.
     */
    ReferenceDTO save(ReferenceDTO referenceDTO);

    /**
     * Updates a reference.
     *
     * @param referenceDTO the entity to update.
     * @return the persisted entity.
     */
    ReferenceDTO update(ReferenceDTO referenceDTO);

    /**
     * Partially updates a reference.
     *
     * @param referenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReferenceDTO> partialUpdate(ReferenceDTO referenceDTO);

    /**
     * Get all the references.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReferenceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" reference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReferenceDTO> findOne(Long id);

    /**
     * Delete the "id" reference.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    Page<ReferenceDTO> searchBytitle(String title, Pageable pageable);
}
