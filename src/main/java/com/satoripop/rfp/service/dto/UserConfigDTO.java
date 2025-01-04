package com.satoripop.rfp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.rfp.domain.UserConfig} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserConfigDTO implements Serializable {

    private Long id;

    private UUID userId;

    private String email;

    private String firstname;

    private String lastname;

    private String username;

    private String password;

    private RoleDTO role;

    private Set<ContextDTO> contexts = new HashSet<>();

    private Set<DocumentDTO> documents = new HashSet<>();

    private Set<ReferenceDTO> references = new HashSet<>();

    private CVDTO cV;

    private Set<TechnologyDTO> technologies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public Set<TechnologyDTO> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<TechnologyDTO> technologies) {
        this.technologies = technologies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserConfigDTO)) {
            return false;
        }

        UserConfigDTO userConfigDTO = (UserConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserConfigDTO{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", email='" + getEmail() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            ", role=" + getRole() +
            ", technologys=" + getTechnologies() +
            "}";
    }
}
