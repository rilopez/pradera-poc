package com.pradera.poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.ZonedDateTime;

import com.pradera.poc.domain.enumeration.BlockType;

/**
 * A Block.
 */
@Entity
@Table(name = "block")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "block")
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

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private ZonedDateTime createdDate;

    @NotNull
    @Column(name = "hash", nullable = false)
    private String hash;

    @ManyToOne
    @JsonIgnoreProperties(value = "blocks", allowSetters = true)
    private Block parent;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "blocks", allowSetters = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BlockType getType() {
        return type;
    }

    public Block type(BlockType type) {
        this.type = type;
        return this;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public Block content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public Block createdDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getHash() {
        return hash;
    }

    public Block hash(String hash) {
        this.hash = hash;
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Block getParent() {
        return parent;
    }

    public Block parent(Block block) {
        this.parent = block;
        return this;
    }

    public void setParent(Block block) {
        this.parent = block;
    }

    public User getUser() {
        return user;
    }

    public Block user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
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
        return 31;
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
