package com.satoripop.rfp.web.rest;

import com.satoripop.rfp.repository.ContextRepository;
import com.satoripop.rfp.service.ContextService;
import com.satoripop.rfp.service.dto.ContextDTO;
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
 * REST controller for managing {@link com.satoripop.rfp.domain.Context}.
 */
@RestController
@RequestMapping("/api/contexts")
public class ContextResource {

    private final Logger log = LoggerFactory.getLogger(ContextResource.class);

    private static final String ENTITY_NAME = "context";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContextService contextService;

    private final ContextRepository contextRepository;

    public ContextResource(ContextService contextService, ContextRepository contextRepository) {
        this.contextService = contextService;
        this.contextRepository = contextRepository;
    }

    /**
     * {@code POST  /contexts} : Create a new context.
     *
     * @param contextDTO the contextDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contextDTO, or with status {@code 400 (Bad Request)} if the context has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContextDTO> createContext(@RequestBody ContextDTO contextDTO) throws URISyntaxException {
        log.debug("REST request to save Context : {}", contextDTO);
        if (contextDTO.getId() != null) {
            throw new BadRequestAlertException("A new context cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contextDTO = contextService.save(contextDTO);
        return ResponseEntity.created(new URI("/api/contexts/" + contextDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contextDTO.getId().toString()))
            .body(contextDTO);
    }

    /**
     * {@code PUT  /contexts/:id} : Updates an existing context.
     *
     * @param id the id of the contextDTO to save.
     * @param contextDTO the contextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contextDTO,
     * or with status {@code 400 (Bad Request)} if the contextDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContextDTO> updateContext(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContextDTO contextDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Context : {}, {}", id, contextDTO);
        if (contextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contextDTO = contextService.update(contextDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contextDTO.getId().toString()))
            .body(contextDTO);
    }

    /**
     * {@code PATCH  /contexts/:id} : Partial updates given fields of an existing context, field will ignore if it is null
     *
     * @param id the id of the contextDTO to save.
     * @param contextDTO the contextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contextDTO,
     * or with status {@code 400 (Bad Request)} if the contextDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contextDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContextDTO> partialUpdateContext(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ContextDTO contextDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Context partially : {}, {}", id, contextDTO);
        if (contextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContextDTO> result = contextService.partialUpdate(contextDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contextDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contexts} : get all the contexts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contexts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContextDTO>> getAllContexts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Contexts");
        Page<ContextDTO> page = contextService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contexts/:id} : get the "id" context.
     *
     * @param id the id of the contextDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contextDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContextDTO> getContext(@PathVariable("id") Long id) {
        log.debug("REST request to get Context : {}", id);
        Optional<ContextDTO> contextDTO = contextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contextDTO);
    }

    /**
     * {@code DELETE  /contexts/:id} : delete the "id" context.
     *
     * @param id the id of the contextDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContext(@PathVariable("id") Long id) {
        log.debug("REST request to delete Context : {}", id);
        contextService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/search/namee")
    public ResponseEntity<List<ContextDTO>> searchContextByName(
        @RequestParam String name,
        @ParameterObject Pageable pageable,
        UriComponentsBuilder uriBuilder
    ) {
        Page<ContextDTO> resultPage = contextService.searchByNamee(name, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder, resultPage);
        return ResponseEntity.ok().headers(headers).body(resultPage.getContent());
    }
}
