package com.satoripop.rfp.web.rest;

import com.satoripop.rfp.repository.UserConfigRepository;
import com.satoripop.rfp.service.UserConfigService;
import com.satoripop.rfp.service.dto.TechnologyDTO;
import com.satoripop.rfp.service.dto.UserConfigDTO;
import com.satoripop.rfp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.satoripop.rfp.domain.UserConfig}.
 */
@RestController
@RequestMapping("/api/user-configs")
public class UserConfigResource {

    private final Logger log = LoggerFactory.getLogger(UserConfigResource.class);

    private static final String ENTITY_NAME = "userConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserConfigService userConfigService;

    private final UserConfigRepository userConfigRepository;

    public UserConfigResource(UserConfigService userConfigService, UserConfigRepository userConfigRepository) {
        this.userConfigService = userConfigService;
        this.userConfigRepository = userConfigRepository;
    }

    /**
     * {@code POST  /user-configs} : Create a new userConfig.
     *
     * @param userConfigDTO the userConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userConfigDTO, or with status {@code 400 (Bad Request)} if the userConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserConfigDTO> createUserConfig(@RequestBody UserConfigDTO userConfigDTO) throws URISyntaxException {
        log.debug("REST request to save UserConfig : {}", userConfigDTO);
        if (userConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new userConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userConfigDTO = userConfigService.save(userConfigDTO);
        return ResponseEntity.created(new URI("/api/user-configs/" + userConfigDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userConfigDTO.getId().toString()))
            .body(userConfigDTO);
    }

    /**
     * {@code PUT  /user-configs/:id} : Updates an existing userConfig.
     *
     * @param id the id of the userConfigDTO to save.
     * @param userConfigDTO the userConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userConfigDTO,
     * or with status {@code 400 (Bad Request)} if the userConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserConfigDTO> updateUserConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserConfigDTO userConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserConfig : {}, {}", id, userConfigDTO);
        if (userConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userConfigDTO = userConfigService.update(userConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userConfigDTO.getId().toString()))
            .body(userConfigDTO);
    }

    /**
     * {@code PATCH  /user-configs/:id} : Partial updates given fields of an existing userConfig, field will ignore if it is null
     *
     * @param id the id of the userConfigDTO to save.
     * @param userConfigDTO the userConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userConfigDTO,
     * or with status {@code 400 (Bad Request)} if the userConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserConfigDTO> partialUpdateUserConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserConfigDTO userConfigDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserConfig partially : {}, {}", id, userConfigDTO);
        if (userConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserConfigDTO> result = userConfigService.partialUpdate(userConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-configs} : get all the userConfigs.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userConfigs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserConfigDTO>> getAllUserConfigs(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("cv-is-null".equals(filter)) {
            log.debug("REST request to get all UserConfigs where cV is null");
            return new ResponseEntity<>(userConfigService.findAllWhereCVIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of UserConfigs");
        Page<UserConfigDTO> page;
        if (eagerload) {
            page = userConfigService.findAllWithEagerRelationships(pageable);
        } else {
            page = userConfigService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-configs/:id} : get the "id" userConfig.
     *
     * @param id the id of the userConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserConfigDTO> getUserConfig(@PathVariable("id") Long id) {
        log.debug("REST request to get UserConfig : {}", id);
        Optional<UserConfigDTO> userConfigDTO = userConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userConfigDTO);
    }

    /**
     * {@code DELETE  /user-configs/:id} : delete the "id" userConfig.
     *
     * @param id the id of the userConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserConfig(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserConfig : {}", id);
        userConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/search/username")
    public ResponseEntity<List<UserConfigDTO>> searchUsersBytitle(
        @RequestParam String username,
        @ParameterObject Pageable pageable,
        UriComponentsBuilder uriBuilder
    ) {
        Page<UserConfigDTO> resultPage = userConfigService.searchBytitle(username, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, resultPage);
        return ResponseEntity.ok().headers(headers).body(resultPage.getContent());
    }
}
