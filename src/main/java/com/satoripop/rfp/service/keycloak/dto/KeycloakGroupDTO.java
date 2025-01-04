package com.satoripop.rfp.service.keycloak.dto;

import java.io.Serializable;
import java.util.List;

public class KeycloakGroupDTO implements Serializable {

    private String id;

    private String name;

    private String path;

    private List<String> realmRoles;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getRealmRoles() {
        return realmRoles;
    }

    public void setRealmRoles(List<String> realmRoles) {
        this.realmRoles = realmRoles;
    }

    @Override
    public String toString() {
        return "GroupDTO{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", path='" + path + '\'' + ", realmRoles=" + realmRoles + '}';
    }
}
