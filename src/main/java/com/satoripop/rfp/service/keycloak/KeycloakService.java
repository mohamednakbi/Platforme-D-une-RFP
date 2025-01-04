package com.satoripop.rfp.service.keycloak;

import com.satoripop.rfp.service.dto.UserConfigDTO;
import com.satoripop.rfp.service.keycloak.dto.Credentials;
import com.satoripop.rfp.service.keycloak.dto.KeycloakTokenResponse;
import com.satoripop.rfp.service.keycloak.dto.KeycloakUserDTO;
import com.satoripop.rfp.service.keycloak.dto.RoleMapping;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakService {

    private final Logger log = LoggerFactory.getLogger(KeycloakService.class);

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String resource;

    @Value("${keycloak.credentials.secret}")
    private String secret;

    @Value("${keycloak.credentials.grant_type}")
    private String grant_type;

    @Value("${keycloak.credentials.grant_type_auth}")
    private String grant_type_auth;

    @Value("${keycloak.credentials.grant_type_refresh}")
    private String grant_type_refresh;

    public KeycloakService() {}

    public String getToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("client_secret", secret);
        requestMap.add("client_id", resource);
        requestMap.add("grant_type", grant_type);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestMap, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<KeycloakTokenResponse> response = restTemplate.exchange(
                authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                HttpMethod.POST,
                request,
                KeycloakTokenResponse.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Authorization successful for Manager");
                KeycloakTokenResponse body = response.getBody();
                if (body != null) {
                    return body.getAccessToken();
                }
            }
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRequestUrl(String path) {
        return StringUtils.removeEnd(authServerUrl, "/") + "/admin/realms/" + realm + "/" + StringUtils.removeStart(path, "/");
    }

    public KeycloakTokenResponse loginUser(UserConfigDTO login) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("client_secret", secret);
        requestMap.add("client_id", resource);
        requestMap.add("grant_type", grant_type_auth);
        requestMap.add("username", login.getEmail());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestMap, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<KeycloakTokenResponse> response = restTemplate.exchange(
                authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                HttpMethod.POST,
                request,
                KeycloakTokenResponse.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Authorization successful for USER");
                KeycloakTokenResponse body = response.getBody();
                if (body != null) {
                    return body;
                }
            }
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RoleMapping> getRolesMapping() {
        String token = this.getToken();
        List<RoleMapping> roleMappings = new ArrayList<>();
        if (token != null) {
            HttpHeaders headers = getHeaders(token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<RoleMapping[]> response = restTemplate.exchange(
                getRequestUrl("/roles"),
                HttpMethod.GET,
                entity,
                RoleMapping[].class
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody().length > 0) {
                roleMappings = Arrays.asList(response.getBody());
                return roleMappings;
            }
        }
        return roleMappings;
    }

    public RoleMapping getRoleMappingByName(String name) {
        String token = this.getToken();
        RoleMapping roleMapping = new RoleMapping();
        if (token != null) {
            HttpHeaders headers = getHeaders(token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<RoleMapping> response = restTemplate.exchange(
                getRequestUrl("/roles/" + name),
                HttpMethod.GET,
                entity,
                RoleMapping.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                roleMapping = response.getBody();
                return roleMapping;
            }
        }
        return roleMapping;
    }

    public void deleteRoleMappingUser(String idKeycloakUser, String roleName) {
        String token = this.getToken();
        List<RoleMapping> roleMappings = this.getRolesMapping();
        List<RoleMapping> roles = new ArrayList<>();
        RoleMapping roleMapping;
        for (RoleMapping mapping : roleMappings) {
            if (mapping.getName().equals(roleName)) {
                roleMapping = mapping;
                roles.add(roleMapping);
            }
        }
        if (token != null) {
            HttpHeaders headers = getHeaders(token);
            HttpEntity<RoleMapping[]> entity = new HttpEntity(roles.toArray(), headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(
                getRequestUrl("/users/" + idKeycloakUser + "/role-mappings/realm"),
                HttpMethod.DELETE,
                entity,
                Void.class
            );
        }
    }

    public void deleteKeycloakUser(String idKeycloakUser) {
        KeycloakUserDTO keycloakUserDTO = null;
        String token = this.getToken();
        if (token != null) {
            HttpHeaders headers = getHeaders(token);
            HttpEntity<Void> entity = new HttpEntity<Void>(headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(getRequestUrl("/users/" + idKeycloakUser), HttpMethod.DELETE, entity, Void.class);
        }
    }

    public KeycloakTokenResponse getAccessTokenWithRefreshToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
        requestMap.add("client_secret", secret);
        requestMap.add("client_id", resource);
        requestMap.add("grant_type", grant_type_refresh);
        requestMap.add("refresh_token", refreshToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(requestMap, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<KeycloakTokenResponse> response = restTemplate.exchange(
                authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                HttpMethod.POST,
                request,
                KeycloakTokenResponse.class
            );
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Keycloak Token Response sent successfully");
                KeycloakTokenResponse body = response.getBody();
                if (body != null) {
                    return body;
                }
            }
        } catch (HttpStatusCodeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean createKeycloakRealmRole(String role) {
        String token = this.getToken();
        Boolean roleCreated = false;
        try {
            if (token != null) {
                HttpHeaders headers = getHeaders(token);
                RoleMapping newRole = new RoleMapping();
                newRole.setName(role);
                HttpEntity<RoleMapping> entity = new HttpEntity<>(newRole, headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Void> response = restTemplate.exchange(getRequestUrl("/roles"), HttpMethod.POST, entity, Void.class);
                roleCreated = response.getStatusCodeValue() == 201 || response.getStatusCodeValue() == 409;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return roleCreated;
    }

    public Boolean createKeycloakRealmRole(RoleMapping role) {
        String token = this.getToken();
        Boolean roleCreated = false;
        try {
            if (token != null) {
                HttpHeaders headers = getHeaders(token);
                HttpEntity<RoleMapping> entity = new HttpEntity<>(role, headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Void> response = restTemplate.exchange(getRequestUrl("/roles"), HttpMethod.POST, entity, Void.class);
                roleCreated = response.getStatusCodeValue() == 201 || response.getStatusCodeValue() == 409;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return roleCreated;
    }

    public Boolean updateKeycloakRole(RoleMapping role) {
        String token = this.getToken();
        Boolean roleCreated = false;
        try {
            if (token != null) {
                HttpHeaders headers = getHeaders(token);
                HttpEntity<RoleMapping> entity = new HttpEntity<>(role, headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Void> response = restTemplate.exchange(
                    getRequestUrl("/roles/" + role.getName()),
                    HttpMethod.PUT,
                    entity,
                    Void.class
                );
                roleCreated = response.getStatusCodeValue() == 201 || response.getStatusCodeValue() == 409;
                System.out.println(response);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return roleCreated;
    }

    public boolean resetKeycloakUserPassword(String idUser, Credentials credentials) {
        String token = getToken();
        if (token != null) {
            HttpHeaders headers = getHeaders(token);
            HttpEntity<Credentials> entity = new HttpEntity<>(credentials, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> response = restTemplate.exchange(
                getRequestUrl("/users/" + idUser + "/reset-password"),
                HttpMethod.PUT,
                entity,
                Object.class
            );
            return response.getStatusCode().is2xxSuccessful();
        }
        return false;
    }

    public void logoutKeycloakUser(String idKeycloakUser) {
        KeycloakUserDTO keycloakUserDTO = null;
        String token = this.getToken();
        if (token != null) {
            HttpHeaders headers = getHeaders(token);
            HttpEntity<Void> entity = new HttpEntity<Void>(headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange(getRequestUrl("/users/" + idKeycloakUser + "/logout"), HttpMethod.POST, entity, Void.class);
        }
    }

    public HttpHeaders getHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }
}
