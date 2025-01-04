package com.satoripop.rfp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.satoripop.rfp.domain.CV} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CVDTO implements Serializable {

    private Long id;

    private String title;

    private String content;

    private UserConfigDTO userConfig;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        if (!(o instanceof CVDTO)) {
            return false;
        }

        CVDTO cVDTO = (CVDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cVDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CVDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", userConfig=" + getUserConfig() +
            "}";
    }
}
