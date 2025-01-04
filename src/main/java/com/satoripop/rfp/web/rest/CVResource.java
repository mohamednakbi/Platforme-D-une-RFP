package com.satoripop.rfp.web.rest;

import com.satoripop.rfp.domain.CV;
import com.satoripop.rfp.repository.CVRepository;
import com.satoripop.rfp.service.CVService;
import com.satoripop.rfp.service.dto.CVDTO;
import com.satoripop.rfp.service.dto.DocumentDTO;
import com.satoripop.rfp.web.rest.errors.BadRequestAlertException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.satoripop.rfp.domain.CV}.
 */
@RestController
@RequestMapping("/api/cvs")
public class CVResource {

    private final Logger log = LoggerFactory.getLogger(CVResource.class);

    private static final String ENTITY_NAME = "cV";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CVService cVService;

    private final CVRepository cVRepository;

    public CVResource(CVService cVService, CVRepository cVRepository) {
        this.cVService = cVService;
        this.cVRepository = cVRepository;
    }

    /**
     * {@code POST  /cvs} : Create a new cV.
     *
     * @param cVDTO the cVDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cVDTO, or with status {@code 400 (Bad Request)} if the cV has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CVDTO> createCV(@RequestBody CVDTO cVDTO) throws URISyntaxException {
        log.debug("REST request to save CV : {}", cVDTO);
        if (cVDTO.getId() != null) {
            throw new BadRequestAlertException("A new cV cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cVDTO = cVService.save(cVDTO);
        return ResponseEntity.created(new URI("/api/cvs/" + cVDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cVDTO.getId().toString()))
            .body(cVDTO);
    }

    /**
     * {@code PUT  /cvs/:id} : Updates an existing cV.
     *
     * @param id the id of the cVDTO to save.
     * @param cVDTO the cVDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cVDTO,
     * or with status {@code 400 (Bad Request)} if the cVDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cVDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CVDTO> updateCV(@PathVariable(value = "id", required = false) final Long id, @RequestBody CVDTO cVDTO)
        throws URISyntaxException {
        log.debug("REST request to update CV : {}, {}", id, cVDTO);
        if (cVDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cVDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cVRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cVDTO = cVService.update(cVDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cVDTO.getId().toString()))
            .body(cVDTO);
    }

    /**
     * {@code PATCH  /cvs/:id} : Partial updates given fields of an existing cV, field will ignore if it is null
     *
     * @param id the id of the cVDTO to save.
     * @param cVDTO the cVDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cVDTO,
     * or with status {@code 400 (Bad Request)} if the cVDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cVDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cVDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CVDTO> partialUpdateCV(@PathVariable(value = "id", required = false) final Long id, @RequestBody CVDTO cVDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update CV partially : {}, {}", id, cVDTO);
        if (cVDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cVDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cVRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CVDTO> result = cVService.partialUpdate(cVDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cVDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cvs} : get all the cVS.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cVS in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CVDTO>> getAllCVS(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of CVS");
        Page<CVDTO> page;
        if (eagerload) {
            page = cVService.findAllWithEagerRelationships(pageable);
        } else {
            page = cVService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cvs/:id} : get the "id" cV.
     *
     * @param id the id of the cVDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cVDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CVDTO> getCV(@PathVariable("id") Long id) {
        log.debug("REST request to get CV : {}", id);
        Optional<CVDTO> cVDTO = cVService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cVDTO);
    }

    /**
     * {@code DELETE  /cvs/:id} : delete the "id" cV.
     *
     * @param id the id of the cVDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCV(@PathVariable("id") Long id) {
        log.debug("REST request to delete CV : {}", id);
        cVService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/search/titlee")
    public ResponseEntity<List<CVDTO>> searchCvBytitle(@RequestParam String title, Pageable pageable, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search CVs by title: {}", title);

        Page<CVDTO> resultPage = cVService.searchByTitle(title, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, resultPage);

        return ResponseEntity.ok().headers(headers).body(resultPage.getContent());
    }
}
