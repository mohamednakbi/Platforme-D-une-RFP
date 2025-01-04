package com.satoripop.rfp.service.impl;

import com.satoripop.rfp.domain.UserConfig;
import com.satoripop.rfp.repository.UserConfigRepository;
import com.satoripop.rfp.service.UserConfigService;
import com.satoripop.rfp.service.dto.UserConfigDTO;
import com.satoripop.rfp.service.keycloak.KeycloakUserService;
import com.satoripop.rfp.service.mapper.UserConfigMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.rfp.domain.UserConfig}.
 */
@Service
@Transactional
public class UserConfigServiceImpl implements UserConfigService {

    private final Logger log = LoggerFactory.getLogger(UserConfigServiceImpl.class);

    private final UserConfigRepository userConfigRepository;

    private final UserConfigMapper userConfigMapper;

    public UserConfigServiceImpl(UserConfigRepository userConfigRepository, UserConfigMapper userConfigMapper) {
        this.userConfigRepository = userConfigRepository;
        this.userConfigMapper = userConfigMapper;
    }

    @Autowired
    private KeycloakUserService keycloakUserService;

    @Override
    public UserConfigDTO save(UserConfigDTO userConfigDTO) {
        log.debug("Request to save UserConfig : {}", userConfigDTO);

        String keycloakUserId = keycloakUserService.createKeycloakUser(userConfigDTO);

        if (keycloakUserId != null) {
            userConfigDTO.setUserId(UUID.fromString(keycloakUserId));

            UserConfig userConfig = userConfigMapper.toEntity(userConfigDTO);

            userConfig = userConfigRepository.save(userConfig);
            keycloakUserService.updateKeycloakUser(userConfigDTO);

            return userConfigMapper.toDto(userConfig);
        } else {
            log.error("Failed to create user in Keycloak.");
            throw new RuntimeException("Failed to create user in Keycloak.");
        }
    }

    @Override
    public UserConfigDTO update(UserConfigDTO userConfigDTO) {
        log.debug("Request to update UserConfig : {}", userConfigDTO);
        keycloakUserService.updateKeycloakUser(userConfigDTO);
        UserConfig userConfig = userConfigMapper.toEntity(userConfigDTO);
        userConfig = userConfigRepository.save(userConfig);
        return userConfigMapper.toDto(userConfig);
    }

    @Override
    public Optional<UserConfigDTO> partialUpdate(UserConfigDTO userConfigDTO) {
        log.debug("Request to partially update UserConfig : {}", userConfigDTO);

        return userConfigRepository
            .findById(userConfigDTO.getId())
            .map(existingUserConfig -> {
                userConfigMapper.partialUpdate(existingUserConfig, userConfigDTO);

                return existingUserConfig;
            })
            .map(userConfigRepository::save)
            .map(userConfigMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserConfigs");
        return userConfigRepository.findAll(pageable).map(userConfigMapper::toDto);
    }

    public Page<UserConfigDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userConfigRepository.findAllWithEagerRelationships(pageable).map(userConfigMapper::toDto);
    }

    /**
     *  Get all the userConfigs where CV is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserConfigDTO> findAllWhereCVIsNull() {
        log.debug("Request to get all userConfigs where CV is null");
        return StreamSupport.stream(userConfigRepository.findAll().spliterator(), false)
            .filter(userConfig -> userConfig.getCV() == null)
            .map(userConfigMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserConfigDTO> findOne(Long id) {
        log.debug("Request to get UserConfig : {}", id);
        return userConfigRepository.findOneWithEagerRelationships(id).map(userConfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserConfig : {}", id);
        Optional<UserConfig> userConfigOptional = userConfigRepository.findById(id);
        if (userConfigOptional.isPresent()) {
            UserConfig userConfig = userConfigOptional.get();
            String keycloakUserId = String.valueOf(userConfig.getUserId());
            keycloakUserService.deleteKeycloakUser(keycloakUserId);
            userConfigRepository.deleteById(id);
        } else {
            log.error("UserConfig with id {} not found.", id);

            throw new RuntimeException("UserConfig with id " + id + " not found.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserConfigDTO> searchBytitle(String username, Pageable pageable) {
        log.debug("Request to search Roles by name : {}", username);
        return userConfigRepository.findByUsernameContainingIgnoreCase(username, pageable).map(userConfigMapper::toDto);
    }
}
