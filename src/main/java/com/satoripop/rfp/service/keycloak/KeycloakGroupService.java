package com.satoripop.rfp.service.keycloak;

import com.satoripop.rfp.service.dto.RoleDTO;
import com.satoripop.rfp.service.keycloak.dto.KeycloakGroupDTO;
import com.satoripop.rfp.service.keycloak.dto.RoleMapping;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakGroupService {

    private final KeycloakService keycloakService;

    private final Logger log = LoggerFactory.getLogger(KeycloakGroupService.class);

    public KeycloakGroupService(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    public List<KeycloakGroupDTO> getGroups() {
        String token = keycloakService.getToken();
        List<KeycloakGroupDTO> keycloakGroupDTOList = new ArrayList<>();
        if (token != null) {
            HttpEntity<Void> entity = new HttpEntity<>(keycloakService.getHeaders(token));
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<KeycloakGroupDTO[]> response = restTemplate.exchange(
                keycloakService.getRequestUrl("/groups?briefRepresentation=false"),
                HttpMethod.GET,
                entity,
                KeycloakGroupDTO[].class
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
                keycloakGroupDTOList = Arrays.asList(response.getBody());
                return keycloakGroupDTOList;
            }
        }
        return keycloakGroupDTOList;
    }

    public KeycloakGroupDTO getGroupById(String id) {
        String token = keycloakService.getToken();
        KeycloakGroupDTO keycloakGroupDTO = new KeycloakGroupDTO();
        try {
            if (token != null) {
                HttpEntity<Void> entity = new HttpEntity<>(keycloakService.getHeaders(token));
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<KeycloakGroupDTO> response = restTemplate.exchange(
                    keycloakService.getRequestUrl("/groups/" + id),
                    HttpMethod.GET,
                    entity,
                    KeycloakGroupDTO.class
                );
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    keycloakGroupDTO = response.getBody();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keycloakGroupDTO;
    }

    public String createKeycloakRole(RoleDTO role) {
        KeycloakGroupDTO keycloakGroupDTO = new KeycloakGroupDTO();
        keycloakGroupDTO.setName(role.getName());
        String id;
        id = keycloakGroupExists(role.getName());
        if (id != null && !id.equals("")) {
            return id;
        }
        String token = keycloakService.getToken();
        try {
            if (token != null) {
                HttpEntity<KeycloakGroupDTO> entity = new HttpEntity<>(keycloakGroupDTO, keycloakService.getHeaders(token));
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<KeycloakGroupDTO> response = restTemplate.exchange(
                    keycloakService.getRequestUrl("/groups"),
                    HttpMethod.POST,
                    entity,
                    KeycloakGroupDTO.class
                );
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("create keycloak Role successful");

                    id = keycloakGroupExists(role.getName());
                    if (role.getAuthorities() != null && role.getAuthorities().size() > 0) {
                        role.setGroupId(UUID.fromString(id));
                        addRealmRolesToGroup(role);
                    }
                    log.info(id);
                    return id;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public void updateKeycloakRole(RoleDTO role) {
        KeycloakGroupDTO keycloakGroupDTO = new KeycloakGroupDTO();
        keycloakGroupDTO.setName(role.getName());
        keycloakGroupDTO.setId(role.getGroupId().toString());
        String token = keycloakService.getToken();
        try {
            if (token != null) {
                HttpEntity<KeycloakGroupDTO> entity = new HttpEntity<>(keycloakGroupDTO, keycloakService.getHeaders(token));
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Void> response = restTemplate.exchange(
                    keycloakService.getRequestUrl("/groups/" + keycloakGroupDTO.getId()),
                    HttpMethod.PUT,
                    entity,
                    Void.class
                );
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("update group keycloak successful");
                    addRealmRolesToGroup(role);
                    removeRolesFromGroup(role);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteKeycloakRole(RoleDTO role) {
        String groupExistsId = keycloakGroupExists(role.getName());
        if (groupExistsId != null && !groupExistsId.equals("") && role.getGroupId().toString().equals(groupExistsId)) {
            try {
                String token = keycloakService.getToken();
                HttpEntity<Void> entity = new HttpEntity<>(keycloakService.getHeaders(token));
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(keycloakService.getRequestUrl("/groups/" + role.getGroupId()), HttpMethod.DELETE, entity, Void.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String keycloakGroupExists(String groupName) {
        List<KeycloakGroupDTO> keycloakGroupDTOList = getGroups();
        return keycloakGroupDTOList
            .stream()
            .filter(dto -> dto.getName().equals(groupName))
            .map(KeycloakGroupDTO::getId)
            .findFirst()
            .orElse("");
    }

    public void addRealmRolesToGroup(RoleDTO roleDTO) {
        createNonExistentRealmRoles(roleDTO.getAuthorities());
        List<RoleMapping> roleMappingList = getKeycloakRolesByGroup(roleDTO);
        String token = keycloakService.getToken();
        try {
            if (token != null) {
                HttpEntity<List<RoleMapping>> entity = new HttpEntity<>(roleMappingList, keycloakService.getHeaders(token));
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Void> response = restTemplate.exchange(
                    keycloakService.getRequestUrl("/groups/" + roleDTO.getGroupId() + "/role-mappings/realm"),
                    HttpMethod.POST,
                    entity,
                    Void.class
                );
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Add Realm Roles to  keycloak group successful");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNonExistentRealmRoles(List<String> roleMappingList) {
        List<RoleMapping> keycloakRoleMappings = keycloakService.getRolesMapping();
        if (roleMappingList != null && !roleMappingList.isEmpty() && keycloakRoleMappings != null && !keycloakRoleMappings.isEmpty()) {
            Set<String> existingRoles = keycloakRoleMappings.stream().map(RoleMapping::getName).collect(Collectors.toSet());
            roleMappingList.stream().filter(role -> !existingRoles.contains(role)).forEach(keycloakService::createKeycloakRealmRole);
        }
    }

    public void removeRolesFromGroup(RoleDTO roleDTO) {
        List<RoleMapping> roleMappingListToRemove = realmRolesToRemove(roleDTO);
        if (roleMappingListToRemove.size() > 0) {
            HttpEntity<List<RoleMapping>> entity = new HttpEntity<>(
                roleMappingListToRemove,
                keycloakService.getHeaders(keycloakService.getToken())
            );
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(
                keycloakService.getRequestUrl("/groups/" + roleDTO.getGroupId() + "/role-mappings/realm"),
                HttpMethod.DELETE,
                entity,
                Void.class
            );
        }
    }

    public List<RoleMapping> realmRolesToRemove(RoleDTO roleDTO) {
        KeycloakGroupDTO keycloakGroupDTO = getGroupById(roleDTO.getGroupId().toString());
        return keycloakGroupDTO
            .getRealmRoles()
            .stream()
            .filter(realmRole -> !roleDTO.getAuthorities().contains(realmRole))
            .map(keycloakService::getRoleMappingByName)
            .collect(Collectors.toList());
    }

    private List<RoleMapping> getKeycloakRolesByGroup(RoleDTO roleDto) {
        KeycloakGroupDTO keycloakGroupDTO = getGroupById(roleDto.getGroupId().toString());
        return roleDto
            .getAuthorities()
            .stream()
            .filter(role -> !keycloakGroupDTO.getRealmRoles().contains(role))
            .map(keycloakService::getRoleMappingByName)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
