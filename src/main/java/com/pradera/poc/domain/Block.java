package com.pradera.poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pradera.poc.domain.enumeration.BlockType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Block.
 */
@Entity
@Table(name = "block")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Block implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private BlockType type;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @NotNull
    @Column(name = "hash", nullable = false)
    private String hash;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parent", "user", "flows" }, allowSetters = true)
    private Block parent;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @ManyToMany(mappedBy = "blocks")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "book", "blocks" }, allowSetters = true)
    private Set<Flow> flows = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Block id(Long id) {
        this.id = id;
        return this;
    }

    public BlockType getType() {
        return this.type;
    }

    public Block type(BlockType type) {
        this.type = type;
        return this;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public Block content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Block createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getHash() {
        return this.hash;
    }

    public Block hash(String hash) {
        this.hash = hash;
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Block getParent() {
        return this.parent;
    }

    public Block parent(Block block) {
        this.setParent(block);
        return this;
    }

    public void setParent(Block block) {
        this.parent = block;
    }

    public User getUser() {
        return this.user;
    }

    public Block user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Flow> getFlows() {
        return this.flows;
    }

    public Block flows(Set<Flow> flows) {
        this.setFlows(flows);
        return this;
    }

    public Block addFlows(Flow flow) {
        this.flows.add(flow);
        flow.getBlocks().add(this);
        return this;
    }

    public Block removeFlows(Flow flow) {
        this.flows.remove(flow);
        flow.getBlocks().remove(this);
        return this;
    }

    public void setFlows(Set<Flow> flows) {
        if (this.flows != null) {
            this.flows.forEach(i -> i.removeBlocks(this));
        }
        if (flows != null) {
            flows.forEach(i -> i.addBlocks(this));
        }
        this.flows = flows;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Block)) {
            return false;
        }
        return id != null && id.equals(((Block) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Block{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", content='" + getContent() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", hash='" + getHash() + "'" +
            "}";
    }
}
