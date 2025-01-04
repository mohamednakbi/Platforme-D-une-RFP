package com.satoripop.rfp.web.rest;

import static com.satoripop.rfp.domain.ReferenceAsserts.*;
import static com.satoripop.rfp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.satoripop.rfp.IntegrationTest;
import com.satoripop.rfp.domain.Reference;
import com.satoripop.rfp.repository.ReferenceRepository;
import com.satoripop.rfp.service.dto.ReferenceDTO;
import com.satoripop.rfp.service.mapper.ReferenceMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReferenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReferenceResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_LASTMODIFIED = "AAAAAAAAAA";
    private static final String UPDATED_LASTMODIFIED = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/references";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReferenceRepository referenceRepository;

    @Autowired
    private ReferenceMapper referenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReferenceMockMvc;

    private Reference reference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reference createEntity(EntityManager em) {
        Reference reference = new Reference().title(DEFAULT_TITLE).content(DEFAULT_CONTENT).lastmodified(DEFAULT_LASTMODIFIED);
        return reference;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reference createUpdatedEntity(EntityManager em) {
        Reference reference = new Reference().title(UPDATED_TITLE).content(UPDATED_CONTENT).lastmodified(UPDATED_LASTMODIFIED);
        return reference;
    }

    @BeforeEach
    public void initTest() {
        reference = createEntity(em);
    }

    @Test
    @Transactional
    void createReference() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reference
        ReferenceDTO referenceDTO = referenceMapper.toDto(reference);
        var returnedReferenceDTO = om.readValue(
            restReferenceMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referenceDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReferenceDTO.class
        );

        // Validate the Reference in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReference = referenceMapper.toEntity(returnedReferenceDTO);
        assertReferenceUpdatableFieldsEquals(returnedReference, getPersistedReference(returnedReference));
    }

    @Test
    @Transactional
    void createReferenceWithExistingId() throws Exception {
        // Create the Reference with an existing ID
        reference.setId(1L);
        ReferenceDTO referenceDTO = referenceMapper.toDto(reference);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReferenceMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reference in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReferences() throws Exception {
        // Initialize the database
        referenceRepository.saveAndFlush(reference);

        // Get all the referenceList
        restReferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reference.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].lastmodified").value(hasItem(DEFAULT_LASTMODIFIED)));
    }

    @Test
    @Transactional
    void getReference() throws Exception {
        // Initialize the database
        referenceRepository.saveAndFlush(reference);

        // Get the reference
        restReferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, reference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reference.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.lastmodified").value(DEFAULT_LASTMODIFIED));
    }

    @Test
    @Transactional
    void getNonExistingReference() throws Exception {
        // Get the reference
        restReferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReference() throws Exception {
        // Initialize the database
        referenceRepository.saveAndFlush(reference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reference
        Reference updatedReference = referenceRepository.findById(reference.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReference are not directly saved in db
        em.detach(updatedReference);
        updatedReference.title(UPDATED_TITLE).content(UPDATED_CONTENT).lastmodified(UPDATED_LASTMODIFIED);
        ReferenceDTO referenceDTO = referenceMapper.toDto(updatedReference);

        restReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referenceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReferenceToMatchAllProperties(updatedReference);
    }

    @Test
    @Transactional
    void putNonExistingReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reference.setId(longCount.incrementAndGet());

        // Create the Reference
        ReferenceDTO referenceDTO = referenceMapper.toDto(reference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, referenceDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reference.setId(longCount.incrementAndGet());

        // Create the Reference
        ReferenceDTO referenceDTO = referenceMapper.toDto(reference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(referenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reference.setId(longCount.incrementAndGet());

        // Create the Reference
        ReferenceDTO referenceDTO = referenceMapper.toDto(reference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferenceMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(referenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReferenceWithPatch() throws Exception {
        // Initialize the database
        referenceRepository.saveAndFlush(reference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reference using partial update
        Reference partialUpdatedReference = new Reference();
        partialUpdatedReference.setId(reference.getId());

        partialUpdatedReference.content(UPDATED_CONTENT).lastmodified(UPDATED_LASTMODIFIED);

        restReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReference.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReference))
            )
            .andExpect(status().isOk());

        // Validate the Reference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReference, reference),
            getPersistedReference(reference)
        );
    }

    @Test
    @Transactional
    void fullUpdateReferenceWithPatch() throws Exception {
        // Initialize the database
        referenceRepository.saveAndFlush(reference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reference using partial update
        Reference partialUpdatedReference = new Reference();
        partialUpdatedReference.setId(reference.getId());

        partialUpdatedReference.title(UPDATED_TITLE).content(UPDATED_CONTENT).lastmodified(UPDATED_LASTMODIFIED);

        restReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReference.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReference))
            )
            .andExpect(status().isOk());

        // Validate the Reference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReferenceUpdatableFieldsEquals(partialUpdatedReference, getPersistedReference(partialUpdatedReference));
    }

    @Test
    @Transactional
    void patchNonExistingReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reference.setId(longCount.incrementAndGet());

        // Create the Reference
        ReferenceDTO referenceDTO = referenceMapper.toDto(reference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, referenceDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reference.setId(longCount.incrementAndGet());

        // Create the Reference
        ReferenceDTO referenceDTO = referenceMapper.toDto(reference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(referenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reference.setId(longCount.incrementAndGet());

        // Create the Reference
        ReferenceDTO referenceDTO = referenceMapper.toDto(reference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReferenceMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(referenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReference() throws Exception {
        // Initialize the database
        referenceRepository.saveAndFlush(reference);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reference
        restReferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, reference.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return referenceRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Reference getPersistedReference(Reference reference) {
        return referenceRepository.findById(reference.getId()).orElseThrow();
    }

    protected void assertPersistedReferenceToMatchAllProperties(Reference expectedReference) {
        assertReferenceAllPropertiesEquals(expectedReference, getPersistedReference(expectedReference));
    }

    protected void assertPersistedReferenceToMatchUpdatableProperties(Reference expectedReference) {
        assertReferenceAllUpdatablePropertiesEquals(expectedReference, getPersistedReference(expectedReference));
    }
}
