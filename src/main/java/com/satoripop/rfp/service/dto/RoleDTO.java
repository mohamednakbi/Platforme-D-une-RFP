package com.satoripop.rfp.service.dto;

import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the {@link com.satoripop.rfp.domain.Role} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoleDTO implements Serializable {

    private Long id;

    private UUID groupId;

    private String name;

    private Boolean permission;

    private List<String> authorities = new ArrayList<>();

    private Set<UserConfigDTO> userConfigs = new HashSet<>();

    @Override
    public String toString() {
        return (
            "RoleDTO{" +
            "id=" +
            id +
            ", groupId=" +
            groupId +
            ", name='" +
            name +
            '\'' +
            ", permission=" +
            permission +
            ", authorities=" +
            authorities +
            '}'
        );
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPermission() {
        return permission;
    }

    public void setPermission(Boolean permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleDTO)) {
            return false;
        }

        RoleDTO roleDTO = (RoleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
