package com.satoripop.rfp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A UserConfig.
 */
@Entity
@Table(name = "user_config")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userConfigs" }, allowSetters = true)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userConfig")
    @JsonIgnoreProperties(value = { "userConfig" }, allowSetters = true)
    private Set<Context> contexts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userConfig")
    @JsonIgnoreProperties(value = { "userConfig" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userConfig")
    @JsonIgnoreProperties(value = { "userConfig" }, allowSetters = true)
    private Set<Reference> references = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_user_config__technologys",
        joinColumns = @JoinColumn(name = "user_config_id"),
        inverseJoinColumns = @JoinColumn(name = "technologys_id")
    )
    @JsonIgnoreProperties(value = { "userConfigs" }, allowSetters = true)
    private Set<Technology> technologies = new HashSet<>();

    @JsonIgnoreProperties(value = { "userConfig" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userConfig")
    private CV cV;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public UserConfig userId(UUID userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return this.email;
    }

    public UserConfig email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public UserConfig firstname(String firstname) {
        this.setFirstname(firstname);
        return this;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public UserConfig lastname(String lastname) {
        this.setLastname(lastname);
        return this;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return this.username;
    }

    public UserConfig username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public UserConfig password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserConfig role(Role role) {
        this.setRole(role);
        return this;
    }

    public Set<Context> getContexts() {
        return this.contexts;
    }

    public void setContexts(Set<Context> contexts) {
        if (this.contexts != null) {
            this.contexts.forEach(i -> i.setUserConfig(null));
        }
        if (contexts != null) {
            contexts.forEach(i -> i.setUserConfig(this));
        }
        this.contexts = contexts;
    }

    public UserConfig contexts(Set<Context> contexts) {
        this.setContexts(contexts);
        return this;
    }

    public UserConfig addContext(Context context) {
        this.contexts.add(context);
        context.setUserConfig(this);
        return this;
    }

    public UserConfig removeContext(Context context) {
        this.contexts.remove(context);
        context.setUserConfig(null);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setUserConfig(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setUserConfig(this));
        }
        this.documents = documents;
    }

    public UserConfig documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public UserConfig addDocument(Document document) {
        this.documents.add(document);
        document.setUserConfig(this);
        return this;
    }

    public UserConfig removeDocument(Document document) {
        this.documents.remove(document);
        document.setUserConfig(null);
        return this;
    }

    public Set<Reference> getReferences() {
        return this.references;
    }

    public void setReferences(Set<Reference> references) {
        if (this.references != null) {
            this.references.forEach(i -> i.setUserConfig(null));
        }
        if (references != null) {
            references.forEach(i -> i.setUserConfig(this));
        }
        this.references = references;
    }

    public UserConfig references(Set<Reference> references) {
        this.setReferences(references);
        return this;
    }

    public UserConfig addReference(Reference reference) {
        this.references.add(reference);
        reference.setUserConfig(this);
        return this;
    }

    public UserConfig removeReference(Reference reference) {
        this.references.remove(reference);
        reference.setUserConfig(null);
        return this;
    }

    public Set<Technology> getTechnologies() {
        return this.technologies;
    }

    public void setTechnologies(Set<Technology> technologies) {
        this.technologies = technologies;
    }

    public UserConfig technologies(Set<Technology> technologies) {
        this.setTechnologies(technologies);
        return this;
    }

    public UserConfig addTechnologys(Technology technology) {
        this.technologies.add(technology);
        return this;
    }

    public UserConfig removeTechnologys(Technology technology) {
        this.technologies.remove(technology);
        return this;
    }

    public CV getCV() {
        return this.cV;
    }

    public void setCV(CV cV) {
        if (this.cV != null) {
            this.cV.setUserConfig(null);
        }
        if (cV != null) {
            cV.setUserConfig(this);
        }
        this.cV = cV;
    }

    public UserConfig cV(CV cV) {
        this.setCV(cV);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((UserConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserConfig{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", email='" + getEmail() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
