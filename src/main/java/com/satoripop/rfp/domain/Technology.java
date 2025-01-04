package com.satoripop.rfp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.satoripop.rfp.domain.enumeration.DocumentType;
import com.satoripop.rfp.domain.enumeration.TechnologyType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Technology.
 */
@Entity
@Table(name = "technology")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Technology implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "version")
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(name = "technology_type")
    private TechnologyType technologyType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_technology__user_configs",
        joinColumns = @JoinColumn(name = "technology_id"),
        inverseJoinColumns = @JoinColumn(name = "user_configs_id")
    )
    @JsonIgnoreProperties(value = { "role", "contexts", "documents", "references", "technologies", "cV" }, allowSetters = true)
    private Set<UserConfig> userConfigs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Technology id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Technology name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public Technology version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Set<UserConfig> getUserConfigs() {
        return this.userConfigs;
    }

    public void setUserConfigs(Set<UserConfig> userConfigs) {
        this.userConfigs = userConfigs;
    }

    public Technology userConfigs(Set<UserConfig> userConfigs) {
        this.setUserConfigs(userConfigs);
        return this;
    }

    public Technology addUserConfigs(UserConfig userConfig) {
        this.userConfigs.add(userConfig);
        return this;
    }

    public Technology removeUserConfigs(UserConfig userConfig) {
        this.userConfigs.remove(userConfig);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Technology)) {
            return false;
        }
        return getId() != null && getId().equals(((Technology) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Technology{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", version='" + getVersion() + "'" +
            "}";
    }
}
