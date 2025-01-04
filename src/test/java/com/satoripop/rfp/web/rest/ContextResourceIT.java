package com.satoripop.rfp.web.rest;

import static com.satoripop.rfp.domain.ContextAsserts.*;
import static com.satoripop.rfp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.satoripop.rfp.IntegrationTest;
import com.satoripop.rfp.domain.Context;
import com.satoripop.rfp.repository.ContextRepository;
import com.satoripop.rfp.service.dto.ContextDTO;
import com.satoripop.rfp.service.mapper.ContextMapper;
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
 * Integration tests for the {@link ContextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContextResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contexts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private ContextMapper contextMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContextMockMvc;

    private Context context;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Context createEntity(EntityManager em) {
        Context context = new Context().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
        return context;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Context createUpdatedEntity(EntityManager em) {
        Context context = new Context().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        return context;
    }

    @BeforeEach
    public void initTest() {
        context = createEntity(em);
    }

    @Test
    @Transactional
    void createContext() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);
        var returnedContextDTO = om.readValue(
            restContextMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contextDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContextDTO.class
        );

        // Validate the Context in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContext = contextMapper.toEntity(returnedContextDTO);
        assertContextUpdatableFieldsEquals(returnedContext, getPersistedContext(returnedContext));
    }

    @Test
    @Transactional
    void createContextWithExistingId() throws Exception {
        // Create the Context with an existing ID
        context.setId(1L);
        ContextDTO contextDTO = contextMapper.toDto(context);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContextMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contextDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContexts() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        // Get all the contextList
        restContextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(context.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        // Get the context
        restContextMockMvc
            .perform(get(ENTITY_API_URL_ID, context.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(context.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingContext() throws Exception {
        // Get the context
        restContextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the context
        Context updatedContext = contextRepository.findById(context.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContext are not directly saved in db
        em.detach(updatedContext);
        updatedContext.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        ContextDTO contextDTO = contextMapper.toDto(updatedContext);

        restContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contextDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contextDTO))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContextToMatchAllProperties(updatedContext);
    }

    @Test
    @Transactional
    void putNonExistingContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contextDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContextWithPatch() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the context using partial update
        Context partialUpdatedContext = new Context();
        partialUpdatedContext.setId(context.getId());

        partialUpdatedContext.description(UPDATED_DESCRIPTION);

        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContext.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContext))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContextUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContext, context), getPersistedContext(context));
    }

    @Test
    @Transactional
    void fullUpdateContextWithPatch() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the context using partial update
        Context partialUpdatedContext = new Context();
        partialUpdatedContext.setId(context.getId());

        partialUpdatedContext.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContext.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContext))
            )
            .andExpect(status().isOk());

        // Validate the Context in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContextUpdatableFieldsEquals(partialUpdatedContext, getPersistedContext(partialUpdatedContext));
    }

    @Test
    @Transactional
    void patchNonExistingContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contextDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContext() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        context.setId(longCount.incrementAndGet());

        // Create the Context
        ContextDTO contextDTO = contextMapper.toDto(context);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContextMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contextDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Context in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContext() throws Exception {
        // Initialize the database
        contextRepository.saveAndFlush(context);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the context
        restContextMockMvc
            .perform(delete(ENTITY_API_URL_ID, context.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contextRepository.count();
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

    protected Context getPersistedContext(Context context) {
        return contextRepository.findById(context.getId()).orElseThrow();
    }

    protected void assertPersistedContextToMatchAllProperties(Context expectedContext) {
        assertContextAllPropertiesEquals(expectedContext, getPersistedContext(expectedContext));
    }

    protected void assertPersistedContextToMatchUpdatableProperties(Context expectedContext) {
        assertContextAllUpdatablePropertiesEquals(expectedContext, getPersistedContext(expectedContext));
    }
}
