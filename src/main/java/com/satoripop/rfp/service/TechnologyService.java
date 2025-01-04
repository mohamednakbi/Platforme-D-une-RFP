package com.satoripop.rfp.service;

import com.satoripop.rfp.service.dto.TechnologyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Interface for managing {@link com.satoripop.rfp.domain.Technology}.
 */
public interface TechnologyService {
    /**
     * Save a technology.
     *
     * @param technologyDTO the entity to save.
     * @return the persisted entity.
     */
    TechnologyDTO save(TechnologyDTO technologyDTO);

    /**
     * Updates a technology.
     *
     * @param technologyDTO the entity to update.
     * @return the persisted entity.
     */
    TechnologyDTO update(TechnologyDTO technologyDTO);

    /**
     * Partially updates a technology.
     *
     * @param technologyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TechnologyDTO> partialUpdate(TechnologyDTO technologyDTO);

    /**
     * Get all the technologies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TechnologyDTO> findAll(Pageable pageable);

    /**
     * Get all the technologies with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TechnologyDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" technology.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TechnologyDTO> findOne(Long id);

    /**
     * Delete the "id" technology.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<TechnologyDTO> searchByNamee(String name, Pageable pageable);
}
