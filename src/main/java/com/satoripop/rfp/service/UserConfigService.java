package com.satoripop.rfp.service;

import com.satoripop.rfp.service.dto.TechnologyDTO;
import com.satoripop.rfp.service.dto.UserConfigDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.rfp.domain.UserConfig}.
 */
public interface UserConfigService {
    /**
     * Save a userConfig.
     *
     * @param userConfigDTO the entity to save.
     * @return the persisted entity.
     */
    UserConfigDTO save(UserConfigDTO userConfigDTO);

    /**
     * Updates a userConfig.
     *
     * @param userConfigDTO the entity to update.
     * @return the persisted entity.
     */
    UserConfigDTO update(UserConfigDTO userConfigDTO);

    /**
     * Partially updates a userConfig.
     *
     * @param userConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserConfigDTO> partialUpdate(UserConfigDTO userConfigDTO);

    /**
     * Get all the userConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserConfigDTO> findAll(Pageable pageable);

    /**
     * Get all the UserConfigDTO where CV is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<UserConfigDTO> findAllWhereCVIsNull();

    /**
     * Get all the userConfigs with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserConfigDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserConfigDTO> findOne(Long id);

    /**
     * Delete the "id" userConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    Page<UserConfigDTO> searchBytitle(String username, Pageable pageable);
}
