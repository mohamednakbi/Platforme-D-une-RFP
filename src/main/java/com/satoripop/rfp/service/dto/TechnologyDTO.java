package com.satoripop.rfp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.satoripop.rfp.domain.Technology} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TechnologyDTO implements Serializable {

    private Long id;

    private String name;

    private String version;

    private Set<UserConfigDTO> userConfigs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<UserConfigDTO> getUserConfigs() {
        return userConfigs;
    }

    public void setUserConfigs(Set<UserConfigDTO> userConfigs) {
        this.userConfigs = userConfigs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TechnologyDTO)) {
            return false;
        }

        TechnologyDTO technologyDTO = (TechnologyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, technologyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "TechnologyDTO{" +
            "id=" +
            getId() +
            ", name='" +
            getName() +
            "'" +
            ", version='" +
            getVersion() +
            "'" +
            ", userConfigs=" +
            getUserConfigs() +
            "}"
        );
    }
}
