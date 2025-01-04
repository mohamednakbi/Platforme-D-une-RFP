package com.satoripop.rfp.service.impl;

import com.satoripop.rfp.domain.Role;
import com.satoripop.rfp.repository.RoleRepository;
import com.satoripop.rfp.service.RoleService;
import com.satoripop.rfp.service.dto.RoleDTO;
import com.satoripop.rfp.service.keycloak.KeycloakGroupService;
import com.satoripop.rfp.service.mapper.RoleMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.rfp.domain.Role}.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    private final KeycloakGroupService keycloakGroupService;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper, KeycloakGroupService keycloakGroupService) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.keycloakGroupService = keycloakGroupService;
    }

    @Override
    public RoleDTO save(RoleDTO roleDTO) {
        log.debug("Request to save Role : {}", roleDTO);

        String keycloakRoleId = keycloakGroupService.createKeycloakRole(roleDTO);

        roleDTO.setGroupId(UUID.fromString(keycloakRoleId));

        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    @Override
    public RoleDTO update(RoleDTO roleDTO) {
        log.debug("Request to update Role : {}", roleDTO);
        keycloakGroupService.updateKeycloakRole(roleDTO);
        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        return roleMapper.toDto(role);
    }

    @Override
    public Optional<RoleDTO> partialUpdate(RoleDTO roleDTO) {
        log.debug("Request to partially update Role : {}", roleDTO);

        return roleRepository
            .findById(roleDTO.getId())
            .map(existingRole -> {
                roleMapper.partialUpdate(existingRole, roleDTO);

                return existingRole;
            })
            .map(roleRepository::save)
            .map(roleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Roles");
        return roleRepository.findAll(pageable).map(roleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RoleDTO> findOne(Long id) {
        log.debug("Request to get Role : {}", id);
        return roleRepository.findById(id).map(roleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Role : {}", id);
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent()) {
            RoleDTO role = roleMapper.toDto(roleOptional.get());

            keycloakGroupService.deleteKeycloakRole(role);

            roleRepository.deleteById(id);
        } else {
            log.error("Role with id {} not found.", id);
            throw new RuntimeException("Role with id " + id + " not found.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleDTO> searchByName(String name, Pageable pageable) {
        log.debug("Request to search Roles by name : {}", name);
        return roleRepository.findByNameContainingIgnoreCase(name, pageable).map(roleMapper::toDto);
    }
}
