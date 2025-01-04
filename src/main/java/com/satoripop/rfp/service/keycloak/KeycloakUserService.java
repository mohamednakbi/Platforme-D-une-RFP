package com.satoripop.rfp.service.keycloak;

import com.satoripop.rfp.service.dto.UserConfigDTO;
import com.satoripop.rfp.service.keycloak.dto.Credentials;
import com.satoripop.rfp.service.keycloak.dto.KeycloakGroupDTO;
import com.satoripop.rfp.service.keycloak.dto.KeycloakUserDTO;
import com.satoripop.rfp.service.keycloak.dto.RoleMapping;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakUserService {

    private final KeycloakService keycloakService;

    private final Logger log = LoggerFactory.getLogger(KeycloakUserService.class);

    public KeycloakUserService(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    public String createUser(UserConfigDTO userConfigDTO) {
        String token = keycloakService.getToken();
        KeycloakUserDTO keycloakUserDTO = prepareKeycloakUser(userConfigDTO);
        try {
            if (token != null) {
                HttpEntity<KeycloakUserDTO> entity = new HttpEntity<>(
                    keycloakUserDTO,
                    keycloakService.getHeaders(keycloakService.getToken())
                );

                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Void> response = restTemplate.exchange(
                    keycloakService.getRequestUrl("/users"),
                    HttpMethod.POST,
                    entity,
                    Void.class
                );
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("User creation successful");
                    return "User created successfully";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "User creation failed";
    }

    public void assignUserToGroup(String userId, String groupId) {
        String token = keycloakService.getToken();
        try {
            if (token != null) {
                HttpEntity<Void> entity = new HttpEntity<>(keycloakService.getHeaders(token));
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Void> response = restTemplate.exchange(
                    keycloakService.getRequestUrl("/users/" + userId + "/groups/" + groupId),
                    HttpMethod.PUT,
                    entity,
                    Void.class
                );
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("User assigned to group successfully");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createKeycloakUser(UserConfigDTO userConfigDTO) {
        KeycloakUserDTO keycloakUserDTO = prepareKeycloakUser(userConfigDTO);
        try {
            HttpEntity<KeycloakUserDTO> entity = new HttpEntity<>(keycloakUserDTO, keycloakService.getHeaders(keycloakService.getToken()));
            if (keycloakUsernameExists(keycloakUserDTO.getUsername()) != null) {
                throw new RuntimeException("Username already exists");
            }
            if (keycloakUserEmailExists(keycloakUserDTO.getEmail()) != null) {
                throw new RuntimeException("Email already exists");
            }
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Void> response = restTemplate.exchange(
                keycloakService.getRequestUrl("/users"),
                HttpMethod.POST,
                entity,
                Void.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("create user keycloak successful");
                return keycloakUsernameExists(keycloakUserDTO.getUsername()).getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateKeycloakUser(UserConfigDTO userConfigDTO) {
        KeycloakUserDTO keycloakUserDTO = prepareKeycloakUser(userConfigDTO);
        List<KeycloakGroupDTO> groupDTOList = getGroupsFromKeycloakUser(keycloakUserDTO);
        if (userConfigDTO.getRole() != null) {
            addKeycloakGroupToUser(userConfigDTO);
            groupDTOList
                .stream()
                .filter(groupDTO -> !groupDTO.getId().equals(userConfigDTO.getRole().getGroupId().toString()))
                .forEach(groupDTO -> removeKeycloakGroupFromUser(userConfigDTO, groupDTO.getId()));
        } else {
            groupDTOList.forEach(groupDTO -> removeKeycloakGroupFromUser(userConfigDTO, groupDTO.getId()));
        }

        HttpEntity<KeycloakUserDTO> entity = new HttpEntity<>(keycloakUserDTO, keycloakService.getHeaders(keycloakService.getToken()));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.exchange(
            keycloakService.getRequestUrl("/users/" + keycloakUserDTO.getId()),
            HttpMethod.PUT,
            entity,
            Void.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("update user keycloak successful");
        }
    }

    public void deleteKeycloakUser(String idKeycloakUser) {
        String token = keycloakService.getToken();
        try {
            if (token != null) {
                HttpEntity<Void> entity = new HttpEntity<>(keycloakService.getHeaders(token));
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.exchange(keycloakService.getRequestUrl("/users/" + idKeycloakUser), HttpMethod.DELETE, entity, Void.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public KeycloakUserDTO keycloakUsernameExists(String username) {
        KeycloakUserDTO keycloakUserDTO = null;
        HttpEntity<KeycloakUserDTO> entity = new HttpEntity<>(keycloakService.getHeaders(keycloakService.getToken()));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KeycloakUserDTO[]> response = restTemplate.exchange(
            keycloakService.getRequestUrl("/users?username=" + username + "&exact=true"),
            HttpMethod.GET,
            entity,
            KeycloakUserDTO[].class
        );
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
            keycloakUserDTO = response.getBody()[0];
        }
        return keycloakUserDTO;
    }

    public KeycloakUserDTO keycloakUserEmailExists(String email) {
        KeycloakUserDTO keycloakUserDTO = null;
        HttpEntity<KeycloakUserDTO> entity = new HttpEntity<>(keycloakService.getHeaders(keycloakService.getToken()));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KeycloakUserDTO[]> response = restTemplate.exchange(
            keycloakService.getRequestUrl("/users?email=" + email + "&exact=true"),
            HttpMethod.GET,
            entity,
            KeycloakUserDTO[].class
        );
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
            keycloakUserDTO = response.getBody()[0];
        }
        return keycloakUserDTO;
    }

    public String disableEnableKeycloakUser(String email) {
        String token = keycloakService.getToken();
        try {
            if (token != null) {
                KeycloakUserDTO keycloakUserDTO = keycloakUserEmailExists(email);
                if (keycloakUserDTO != null) {
                    if (!keycloakUserDTO.getEnabled()) {
                        keycloakUserDTO.setEnabled(true);
                        HttpEntity<KeycloakUserDTO> entity = new HttpEntity<>(
                            keycloakUserDTO,
                            keycloakService.getHeaders(keycloakService.getToken())
                        );
                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<Void> response = restTemplate.exchange(
                            keycloakService.getRequestUrl("/users/" + keycloakUserDTO.getId()),
                            HttpMethod.PUT,
                            entity,
                            Void.class
                        );
                        if (response.getStatusCode().is2xxSuccessful()) {
                            log.info("Update keycloak user with success");
                            return "User disabled with success !";
                        }
                    } else {
                        keycloakUserDTO.setEnabled(false);
                        HttpEntity<KeycloakUserDTO> entity = new HttpEntity<>(
                            keycloakUserDTO,
                            keycloakService.getHeaders(keycloakService.getToken())
                        );
                        RestTemplate restTemplate = new RestTemplate();
                        ResponseEntity<Void> response = restTemplate.exchange(
                            keycloakService.getRequestUrl("/users/" + keycloakUserDTO.getId()),
                            HttpMethod.PUT,
                            entity,
                            Void.class
                        );
                        if (response.getStatusCode().is2xxSuccessful()) {
                            log.info("Update keycloak user with success");
                            return "User disabled with success !";
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeycloakUserDTO getUserFromKeycloakById(String id) {
        KeycloakUserDTO keycloakUserDTO = null;
        RestTemplate restTemplate = new RestTemplate();
        String fetchUserUrl = keycloakService.getRequestUrl("/users?id" + id);
        HttpEntity<Void> requestEntity = new HttpEntity<>(keycloakService.getHeaders(keycloakService.getToken()));
        ResponseEntity<KeycloakUserDTO[]> response = restTemplate.exchange(
            fetchUserUrl,
            HttpMethod.GET,
            requestEntity,
            KeycloakUserDTO[].class
        );
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
            for (KeycloakUserDTO keycloakUserDTO1 : response.getBody()) {
                if (keycloakUserDTO1.getId().equals(id)) {
                    keycloakUserDTO = keycloakUserDTO1;
                }
            }
        }
        return keycloakUserDTO;
    }

    public void addRoleMappingToUser(String idKeycloakUser, String roleName) {
        List<RoleMapping> roleMappings = keycloakService.getRolesMapping();
        List<RoleMapping> roles = new ArrayList<>();
        RoleMapping roleMapping;
        for (RoleMapping mapping : roleMappings) {
            if (mapping.getName().equals(roleName)) {
                roleMapping = mapping;
                roles.add(roleMapping);
            }
        }
        HttpEntity<RoleMapping[]> entity = new HttpEntity(roles.toArray(), keycloakService.getHeaders(keycloakService.getToken()));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(
            keycloakService.getRequestUrl("/users/" + idKeycloakUser + "/role-mappings/realm"),
            HttpMethod.POST,
            entity,
            Void.class
        );
    }

    public KeycloakUserDTO getUserFromKeycloakByUsername(String username) {
        KeycloakUserDTO keycloakUserDTO = null;
        HttpEntity<KeycloakUserDTO> entity = new HttpEntity<>(keycloakService.getHeaders(keycloakService.getToken()));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KeycloakUserDTO[]> response = restTemplate.exchange(
            keycloakService.getRequestUrl("/users?username=" + username),
            HttpMethod.GET,
            entity,
            KeycloakUserDTO[].class
        );

        Optional<KeycloakUserDTO[]> responseBody = Optional.ofNullable(response.getBody());
        if (response.getStatusCode().is2xxSuccessful() && responseBody.isPresent()) {
            List<KeycloakUserDTO> keycloakUserDTOS = new ArrayList<>(List.of(responseBody.get()));
            keycloakUserDTO = keycloakUserDTOS
                .stream()
                .filter(userDTO -> userDTO.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
        }
        return keycloakUserDTO;
    }

    public void addKeycloakGroupToUser(UserConfigDTO userConfigDTO) {
        HttpEntity<Void> entity = new HttpEntity<>(keycloakService.getHeaders(keycloakService.getToken()));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.exchange(
            keycloakService.getRequestUrl("/users/" + userConfigDTO.getUserId() + "/groups/" + userConfigDTO.getRole().getGroupId()),
            HttpMethod.PUT,
            entity,
            Void.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("update user keycloak successful");
        }
    }

    public void removeKeycloakGroupFromUser(UserConfigDTO userConfigDTO, String group) {
        HttpEntity<Void> entity = new HttpEntity<>(keycloakService.getHeaders(keycloakService.getToken()));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(
            keycloakService.getRequestUrl("/users/" + userConfigDTO.getUserId() + "/groups/" + group),
            HttpMethod.DELETE,
            entity,
            Void.class
        );
    }

    public List<KeycloakGroupDTO> getGroupsFromKeycloakUser(KeycloakUserDTO keycloakUserDTO) {
        List<KeycloakGroupDTO> groupDTOList = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String fetchUserUrl = keycloakService.getRequestUrl("/users/" + keycloakUserDTO.getId() + "/groups");
        HttpEntity<Void> requestEntity = new HttpEntity<>(keycloakService.getHeaders(keycloakService.getToken()));
        ResponseEntity<KeycloakGroupDTO[]> response = restTemplate.exchange(
            fetchUserUrl,
            HttpMethod.GET,
            requestEntity,
            KeycloakGroupDTO[].class
        );
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().length > 0) {
            groupDTOList = Arrays.asList(response.getBody());
        }
        return groupDTOList;
    }

    private KeycloakUserDTO prepareKeycloakUser(UserConfigDTO userConfigDTO) {
        KeycloakUserDTO keycloakUserDTO = new KeycloakUserDTO();
        if (userConfigDTO.getUserId() != null) {
            keycloakUserDTO.setId(userConfigDTO.getUserId().toString());
        }
        keycloakUserDTO.setUsername(userConfigDTO.getUsername());
        keycloakUserDTO.setEmail(userConfigDTO.getEmail());
        return keycloakUserDTO;
    }

    public void sendEmailPasswordInitializationLink(String userId) {
        String[] requiredActions = { "UPDATE_PASSWORD" };
        HttpEntity<String[]> entity = new HttpEntity<>(requiredActions, keycloakService.getHeaders(keycloakService.getToken()));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.exchange(
            keycloakService.getRequestUrl("/users/" + userId + "/execute-actions-email"),
            HttpMethod.PUT,
            entity,
            Void.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("email initialization link sent successfully");
        } else log.error("failed to send email password initialization link");
    }

    public void resetKeycloakUserPassword(String userId, Credentials credentials) {
        HttpEntity<Credentials> entity = new HttpEntity<>(credentials, keycloakService.getHeaders(keycloakService.getToken()));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response = restTemplate.exchange(
            keycloakService.getRequestUrl("/users/" + userId + "/reset-password"),
            HttpMethod.PUT,
            entity,
            Object.class
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("password updated successfully");
        } else log.error("failed to update password");
    }
}
