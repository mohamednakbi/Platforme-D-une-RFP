package com.satoripop.rfp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.satoripop.rfp.domain.Context} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContextDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private UserConfigDTO userConfig;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserConfigDTO getUserConfig() {
        return userConfig;
    }

    public void setUserConfig(UserConfigDTO userConfig) {
        this.userConfig = userConfig;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContextDTO)) {
            return false;
        }

        ContextDTO contextDTO = (ContextDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contextDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContextDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", userConfig=" + getUserConfig() +
            "}";
    }
}
