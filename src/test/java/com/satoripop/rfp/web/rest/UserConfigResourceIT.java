package com.satoripop.rfp.web.rest;

import static com.satoripop.rfp.domain.UserConfigAsserts.*;
import static com.satoripop.rfp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.satoripop.rfp.IntegrationTest;
import com.satoripop.rfp.domain.UserConfig;
import com.satoripop.rfp.repository.UserConfigRepository;
import com.satoripop.rfp.service.UserConfigService;
import com.satoripop.rfp.service.dto.UserConfigDTO;
import com.satoripop.rfp.service.mapper.UserConfigMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
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
 * Integration tests for the {@link UserConfigResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserConfigResourceIT {

    private static final UUID DEFAULT_USER_ID = UUID.randomUUID();
    private static final UUID UPDATED_USER_ID = UUID.randomUUID();

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserConfigRepository userConfigRepository;

    @Mock
    private UserConfigRepository userConfigRepositoryMock;

    @Autowired
    private UserConfigMapper userConfigMapper;

    @Mock
    private UserConfigService userConfigServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserConfigMockMvc;

    private UserConfig userConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserConfig createEntity(EntityManager em) {
        UserConfig userConfig = new UserConfig()
            .userId(DEFAULT_USER_ID)
            .email(DEFAULT_EMAIL)
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD);
        return userConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserConfig createUpdatedEntity(EntityManager em) {
        UserConfig userConfig = new UserConfig()
            .userId(UPDATED_USER_ID)
            .email(UPDATED_EMAIL)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD);
        return userConfig;
    }

    @BeforeEach
    public void initTest() {
        userConfig = createEntity(em);
    }

    @Test
    @Transactional
    void createUserConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserConfig
        UserConfigDTO userConfigDTO = userConfigMapper.toDto(userConfig);
        var returnedUserConfigDTO = om.readValue(
            restUserConfigMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConfigDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserConfigDTO.class
        );

        // Validate the UserConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserConfig = userConfigMapper.toEntity(returnedUserConfigDTO);
        assertUserConfigUpdatableFieldsEquals(returnedUserConfig, getPersistedUserConfig(returnedUserConfig));
    }

    @Test
    @Transactional
    void createUserConfigWithExistingId() throws Exception {
        // Create the UserConfig with an existing ID
        userConfig.setId(1L);
        UserConfigDTO userConfigDTO = userConfigMapper.toDto(userConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserConfigMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserConfigs() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        // Get all the userConfigList
        restUserConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserConfigsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userConfigServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserConfigMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userConfigServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserConfigsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userConfigServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserConfigMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userConfigRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserConfig() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        // Get the userConfig
        restUserConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, userConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userConfig.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingUserConfig() throws Exception {
        // Get the userConfig
        restUserConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserConfig() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userConfig
        UserConfig updatedUserConfig = userConfigRepository.findById(userConfig.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserConfig are not directly saved in db
        em.detach(updatedUserConfig);
        updatedUserConfig
            .userId(UPDATED_USER_ID)
            .email(UPDATED_EMAIL)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD);
        UserConfigDTO userConfigDTO = userConfigMapper.toDto(updatedUserConfig);

        restUserConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userConfigDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserConfigToMatchAllProperties(updatedUserConfig);
    }

    @Test
    @Transactional
    void putNonExistingUserConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfig.setId(longCount.incrementAndGet());

        // Create the UserConfig
        UserConfigDTO userConfigDTO = userConfigMapper.toDto(userConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userConfigDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfig.setId(longCount.incrementAndGet());

        // Create the UserConfig
        UserConfigDTO userConfigDTO = userConfigMapper.toDto(userConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfig.setId(longCount.incrementAndGet());

        // Create the UserConfig
        UserConfigDTO userConfigDTO = userConfigMapper.toDto(userConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserConfigWithPatch() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userConfig using partial update
        UserConfig partialUpdatedUserConfig = new UserConfig();
        partialUpdatedUserConfig.setId(userConfig.getId());

        partialUpdatedUserConfig.username(UPDATED_USERNAME);

        restUserConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserConfig.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserConfig))
            )
            .andExpect(status().isOk());

        // Validate the UserConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserConfig, userConfig),
            getPersistedUserConfig(userConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserConfigWithPatch() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userConfig using partial update
        UserConfig partialUpdatedUserConfig = new UserConfig();
        partialUpdatedUserConfig.setId(userConfig.getId());

        partialUpdatedUserConfig
            .userId(UPDATED_USER_ID)
            .email(UPDATED_EMAIL)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD);

        restUserConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserConfig.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserConfig))
            )
            .andExpect(status().isOk());

        // Validate the UserConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserConfigUpdatableFieldsEquals(partialUpdatedUserConfig, getPersistedUserConfig(partialUpdatedUserConfig));
    }

    @Test
    @Transactional
    void patchNonExistingUserConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfig.setId(longCount.incrementAndGet());

        // Create the UserConfig
        UserConfigDTO userConfigDTO = userConfigMapper.toDto(userConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userConfigDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfig.setId(longCount.incrementAndGet());

        // Create the UserConfig
        UserConfigDTO userConfigDTO = userConfigMapper.toDto(userConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userConfig.setId(longCount.incrementAndGet());

        // Create the UserConfig
        UserConfigDTO userConfigDTO = userConfigMapper.toDto(userConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserConfig() throws Exception {
        // Initialize the database
        userConfigRepository.saveAndFlush(userConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userConfig
        restUserConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, userConfig.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userConfigRepository.count();
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

    protected UserConfig getPersistedUserConfig(UserConfig userConfig) {
        return userConfigRepository.findById(userConfig.getId()).orElseThrow();
    }

    protected void assertPersistedUserConfigToMatchAllProperties(UserConfig expectedUserConfig) {
        assertUserConfigAllPropertiesEquals(expectedUserConfig, getPersistedUserConfig(expectedUserConfig));
    }

    protected void assertPersistedUserConfigToMatchUpdatableProperties(UserConfig expectedUserConfig) {
        assertUserConfigAllUpdatablePropertiesEquals(expectedUserConfig, getPersistedUserConfig(expectedUserConfig));
    }
}
