package com.satoripop.rfp.web.rest;

import static com.satoripop.rfp.domain.CVAsserts.*;
import static com.satoripop.rfp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.satoripop.rfp.IntegrationTest;
import com.satoripop.rfp.domain.CV;
import com.satoripop.rfp.repository.CVRepository;
import com.satoripop.rfp.service.CVService;
import com.satoripop.rfp.service.dto.CVDTO;
import com.satoripop.rfp.service.mapper.CVMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CVResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CVResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cvs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CVRepository cVRepository;

    @Mock
    private CVRepository cVRepositoryMock;

    @Autowired
    private CVMapper cVMapper;

    @Mock
    private CVService cVServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCVMockMvc;

    private CV cV;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CV createEntity(EntityManager em) {
        CV cV = new CV().title(DEFAULT_TITLE).content(DEFAULT_CONTENT);
        return cV;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CV createUpdatedEntity(EntityManager em) {
        CV cV = new CV().title(UPDATED_TITLE).content(UPDATED_CONTENT);
        return cV;
    }

    @BeforeEach
    public void initTest() {
        cV = createEntity(em);
    }

    @Test
    @Transactional
    void createCV() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CV
        CVDTO cVDTO = cVMapper.toDto(cV);
        var returnedCVDTO = om.readValue(
            restCVMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cVDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CVDTO.class
        );

        // Validate the CV in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCV = cVMapper.toEntity(returnedCVDTO);
        assertCVUpdatableFieldsEquals(returnedCV, getPersistedCV(returnedCV));
    }

    @Test
    @Transactional
    void createCVWithExistingId() throws Exception {
        // Create the CV with an existing ID
        cV.setId(1L);
        CVDTO cVDTO = cVMapper.toDto(cV);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCVMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cVDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CV in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCVS() throws Exception {
        // Initialize the database
        cVRepository.saveAndFlush(cV);

        // Get all the cVList
        restCVMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cV.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCVSWithEagerRelationshipsIsEnabled() throws Exception {
        when(cVServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCVMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cVServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCVSWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cVServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCVMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cVRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCV() throws Exception {
        // Initialize the database
        cVRepository.saveAndFlush(cV);

        // Get the cV
        restCVMockMvc
            .perform(get(ENTITY_API_URL_ID, cV.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cV.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingCV() throws Exception {
        // Get the cV
        restCVMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCV() throws Exception {
        // Initialize the database
        cVRepository.saveAndFlush(cV);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cV
        CV updatedCV = cVRepository.findById(cV.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCV are not directly saved in db
        em.detach(updatedCV);
        updatedCV.title(UPDATED_TITLE).content(UPDATED_CONTENT);
        CVDTO cVDTO = cVMapper.toDto(updatedCV);

        restCVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cVDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cVDTO))
            )
            .andExpect(status().isOk());

        // Validate the CV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCVToMatchAllProperties(updatedCV);
    }

    @Test
    @Transactional
    void putNonExistingCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cV.setId(longCount.incrementAndGet());

        // Create the CV
        CVDTO cVDTO = cVMapper.toDto(cV);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cVDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cVDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cV.setId(longCount.incrementAndGet());

        // Create the CV
        CVDTO cVDTO = cVMapper.toDto(cV);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCVMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cVDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cV.setId(longCount.incrementAndGet());

        // Create the CV
        CVDTO cVDTO = cVMapper.toDto(cV);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCVMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cVDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCVWithPatch() throws Exception {
        // Initialize the database
        cVRepository.saveAndFlush(cV);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cV using partial update
        CV partialUpdatedCV = new CV();
        partialUpdatedCV.setId(cV.getId());

        partialUpdatedCV.content(UPDATED_CONTENT);

        restCVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCV.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCV))
            )
            .andExpect(status().isOk());

        // Validate the CV in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCVUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCV, cV), getPersistedCV(cV));
    }

    @Test
    @Transactional
    void fullUpdateCVWithPatch() throws Exception {
        // Initialize the database
        cVRepository.saveAndFlush(cV);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cV using partial update
        CV partialUpdatedCV = new CV();
        partialUpdatedCV.setId(cV.getId());

        partialUpdatedCV.title(UPDATED_TITLE).content(UPDATED_CONTENT);

        restCVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCV.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCV))
            )
            .andExpect(status().isOk());

        // Validate the CV in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCVUpdatableFieldsEquals(partialUpdatedCV, getPersistedCV(partialUpdatedCV));
    }

    @Test
    @Transactional
    void patchNonExistingCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cV.setId(longCount.incrementAndGet());

        // Create the CV
        CVDTO cVDTO = cVMapper.toDto(cV);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cVDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cVDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cV.setId(longCount.incrementAndGet());

        // Create the CV
        CVDTO cVDTO = cVMapper.toDto(cV);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCVMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cVDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCV() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cV.setId(longCount.incrementAndGet());

        // Create the CV
        CVDTO cVDTO = cVMapper.toDto(cV);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCVMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cVDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CV in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCV() throws Exception {
        // Initialize the database
        cVRepository.saveAndFlush(cV);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cV
        restCVMockMvc
            .perform(delete(ENTITY_API_URL_ID, cV.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cVRepository.count();
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

    protected CV getPersistedCV(CV cV) {
        return cVRepository.findById(cV.getId()).orElseThrow();
    }

    protected void assertPersistedCVToMatchAllProperties(CV expectedCV) {
        assertCVAllPropertiesEquals(expectedCV, getPersistedCV(expectedCV));
    }

    protected void assertPersistedCVToMatchUpdatableProperties(CV expectedCV) {
        assertCVAllUpdatablePropertiesEquals(expectedCV, getPersistedCV(expectedCV));
    }
}
