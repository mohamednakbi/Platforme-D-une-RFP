package com.satoripop.rfp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "name")
    private String name;

    @Column(name = "permission")
    private Boolean permission;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    @JsonIgnoreProperties(value = { "role", "contexts", "documents", "references", "technologys", "cV" }, allowSetters = true)
    private Set<UserConfig> userConfigs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Role id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getGroupId() {
        return this.groupId;
    }

    public Role groupId(UUID groupId) {
        this.setGroupId(groupId);
        return this;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return this.name;
    }

    public Role name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPermission() {
        return this.permission;
    }

    public Role permission(Boolean permission) {
        this.setPermission(permission);
        return this;
    }

    public void setPermission(Boolean permission) {
        this.permission = permission;
    }

    public Set<UserConfig> getUserConfigs() {
        return this.userConfigs;
    }

    public void setUserConfigs(Set<UserConfig> userConfigs) {
        if (this.userConfigs != null) {
            this.userConfigs.forEach(i -> i.setRole(null));
        }
        if (userConfigs != null) {
            userConfigs.forEach(i -> i.setRole(this));
        }
        this.userConfigs = userConfigs;
    }

    public Role userConfigs(Set<UserConfig> userConfigs) {
        this.setUserConfigs(userConfigs);
        return this;
    }

    public Role addUserConfig(UserConfig userConfig) {
        this.userConfigs.add(userConfig);
        userConfig.setRole(this);
        return this;
    }

    public Role removeUserConfig(UserConfig userConfig) {
        this.userConfigs.remove(userConfig);
        userConfig.setRole(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return getId() != null && getId().equals(((Role) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", groupId='" + getGroupId() + "'" +
            ", name='" + getName() + "'" +
            ", permission='" + getPermission() + "'" +
            "}";
    }
}
