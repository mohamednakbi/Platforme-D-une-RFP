package com.satoripop.rfp.web.rest;

import com.satoripop.rfp.repository.ReferenceRepository;
import com.satoripop.rfp.service.ReferenceService;
import com.satoripop.rfp.service.dto.ReferenceDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.satoripop.rfp.domain.Reference}.
 */
@RestController
@RequestMapping("/api/references")
public class ReferenceResource {

    private final Logger log = LoggerFactory.getLogger(ReferenceResource.class);

    private static final String ENTITY_NAME = "reference";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferenceService referenceService;

    private final ReferenceRepository referenceRepository;

    public ReferenceResource(ReferenceService referenceService, ReferenceRepository referenceRepository) {
        this.referenceService = referenceService;
        this.referenceRepository = referenceRepository;
    }

    /**
     * {@code POST  /references} : Create a new reference.
     *
     * @param referenceDTO the referenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referenceDTO, or with status {@code 400 (Bad Request)} if the reference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReferenceDTO> createReference(@RequestBody ReferenceDTO referenceDTO) throws URISyntaxException {
        log.debug("REST request to save Reference : {}", referenceDTO);
        if (referenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new reference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        referenceDTO = referenceService.save(referenceDTO);
        return ResponseEntity.created(new URI("/api/references/" + referenceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, referenceDTO.getId().toString()))
            .body(referenceDTO);
    }

    /**
     * {@code PUT  /references/:id} : Updates an existing reference.
     *
     * @param id the id of the referenceDTO to save.
     * @param referenceDTO the referenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referenceDTO,
     * or with status {@code 400 (Bad Request)} if the referenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReferenceDTO> updateReference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReferenceDTO referenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Reference : {}, {}", id, referenceDTO);
        if (referenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        referenceDTO = referenceService.update(referenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referenceDTO.getId().toString()))
            .body(referenceDTO);
    }

    /**
     * {@code PATCH  /references/:id} : Partial updates given fields of an existing reference, field will ignore if it is null
     *
     * @param id the id of the referenceDTO to save.
     * @param referenceDTO the referenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referenceDTO,
     * or with status {@code 400 (Bad Request)} if the referenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the referenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the referenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReferenceDTO> partialUpdateReference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReferenceDTO referenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reference partially : {}, {}", id, referenceDTO);
        if (referenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, referenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!referenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReferenceDTO> result = referenceService.partialUpdate(referenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /references} : get all the references.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of references in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReferenceDTO>> getAllReferences(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of References");
        Page<ReferenceDTO> page = referenceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /references/:id} : get the "id" reference.
     *
     * @param id the id of the referenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReferenceDTO> getReference(@PathVariable("id") Long id) {
        log.debug("REST request to get Reference : {}", id);
        Optional<ReferenceDTO> referenceDTO = referenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(referenceDTO);
    }

    /**
     * {@code DELETE  /references/:id} : delete the "id" reference.
     *
     * @param id the id of the referenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReference(@PathVariable("id") Long id) {
        log.debug("REST request to delete Reference : {}", id);
        referenceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<ReferenceDTO>> searchReferenceBytitle(
        @RequestParam String title,
        @ParameterObject Pageable pageable,
        UriComponentsBuilder uriBuilder
    ) {
        Page<ReferenceDTO> resultPage = referenceService.searchBytitle(title, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, resultPage);
        return ResponseEntity.ok().headers(headers).body(resultPage.getContent());
    }
}
