package com.satoripop.rfp.service.keycloak.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class KeycloakUserDTO {

    private String id;

    private String email;
    private String username;

    private Boolean enabled;
    private Boolean emailVerified;

    private List<Credentials> credentials;

    private Instant createdTimestamp;

    private int notBefore;
    private List<String> requiredActions = new ArrayList<String>();

    public KeycloakUserDTO() {
        this.enabled = true;
        this.emailVerified = true;
    }

    public KeycloakUserDTO(
        String email,
        String username,
        Boolean enabled,
        Boolean emailVerified,
        List<Credentials> credentials,
        Instant createdTimestamp
    ) {
        this.email = email;
        this.username = username;
        this.enabled = true;
        this.emailVerified = true;
        this.credentials = credentials;
        this.createdTimestamp = createdTimestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Instant getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Instant createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public int getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(int notBefore) {
        this.notBefore = notBefore;
    }

    public List<String> getRequiredActions() {
        return requiredActions;
    }

    public void setRequiredActions(List<String> requiredActions) {
        this.requiredActions = requiredActions;
    }

    public List<Credentials> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<Credentials> credentials) {
        this.credentials = credentials;
    }
}
