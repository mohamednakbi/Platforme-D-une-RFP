package com.satoripop.rfp.web.rest;

import com.satoripop.rfp.repository.TechnologyRepository;
import com.satoripop.rfp.service.TechnologyService;
import com.satoripop.rfp.service.dto.TechnologyDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.satoripop.rfp.domain.Technology}.
 */
@RestController
@RequestMapping("/api/technologies")
public class TechnologyResource {

    private final Logger log = LoggerFactory.getLogger(TechnologyResource.class);

    private static final String ENTITY_NAME = "technology";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechnologyService technologyService;

    private final TechnologyRepository technologyRepository;

    public TechnologyResource(TechnologyService technologyService, TechnologyRepository technologyRepository) {
        this.technologyService = technologyService;
        this.technologyRepository = technologyRepository;
    }

    /**
     * {@code POST  /technologies} : Create a new technology.
     *
     * @param technologyDTO the technologyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new technologyDTO, or with status {@code 400 (Bad Request)} if the technology has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TechnologyDTO> createTechnology(@RequestBody TechnologyDTO technologyDTO) throws URISyntaxException {
        log.debug("REST request to save Technology : {}", technologyDTO);
        if (technologyDTO.getId() != null) {
            throw new BadRequestAlertException("A new technology cannot already have an ID", ENTITY_NAME, "idexists");
        }
        technologyDTO = technologyService.save(technologyDTO);
        return ResponseEntity.created(new URI("/api/technologies/" + technologyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, technologyDTO.getId().toString()))
            .body(technologyDTO);
    }

    /**
     * {@code PUT  /technologies/:id} : Updates an existing technology.
     *
     * @param id the id of the technologyDTO to save.
     * @param technologyDTO the technologyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technologyDTO,
     * or with status {@code 400 (Bad Request)} if the technologyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the technologyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechnologyDTO> updateTechnology(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TechnologyDTO technologyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Technology : {}, {}", id, technologyDTO);
        if (technologyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technologyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!technologyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        technologyDTO = technologyService.update(technologyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, technologyDTO.getId().toString()))
            .body(technologyDTO);
    }

    /**
     * {@code PATCH  /technologies/:id} : Partial updates given fields of an existing technology, field will ignore if it is null
     *
     * @param id the id of the technologyDTO to save.
     * @param technologyDTO the technologyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technologyDTO,
     * or with status {@code 400 (Bad Request)} if the technologyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the technologyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the technologyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TechnologyDTO> partialUpdateTechnology(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TechnologyDTO technologyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Technology partially : {}, {}", id, technologyDTO);
        if (technologyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technologyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!technologyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TechnologyDTO> result = technologyService.partialUpdate(technologyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, technologyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /technologies} : get all the technologies.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of technologies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TechnologyDTO>> getAllTechnologies(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Technologies");
        Page<TechnologyDTO> page;
        if (eagerload) {
            page = technologyService.findAllWithEagerRelationships(pageable);
        } else {
            page = technologyService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /technologies/:id} : get the "id" technology.
     *
     * @param id the id of the technologyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the technologyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechnologyDTO> getTechnology(@PathVariable("id") Long id) {
        log.debug("REST request to get Technology : {}", id);
        Optional<TechnologyDTO> technologyDTO = technologyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(technologyDTO);
    }

    /**
     * {@code DELETE  /technologies/:id} : delete the "id" technology.
     *
     * @param id the id of the technologyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnology(@PathVariable("id") Long id) {
        log.debug("REST request to delete Technology : {}", id);
        technologyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/search/namee")
    public ResponseEntity<List<TechnologyDTO>> searchTechnologiesByName(
        @RequestParam String name,
        @ParameterObject Pageable pageable,
        UriComponentsBuilder uriBuilder
    ) {
        Page<TechnologyDTO> resultPage = technologyService.searchByNamee(name, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, resultPage);
        return ResponseEntity.ok().headers(headers).body(resultPage.getContent());
    }
}
