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
    @JsonIgnoreProperties(value = { "parent", "user", "parentBlocks", "flows" }, allowSetters = true)
    private Block parent;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @OneToMany(mappedBy = "parent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parent", "user", "parentBlocks", "flows" }, allowSetters = true)
    private Set<Block> parentBlocks = new HashSet<>();

    @OneToMany(mappedBy = "block")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "flow", "block" }, allowSetters = true)
    private Set<FlowBlock> flows = new HashSet<>();

    public Block() {}

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

    public Set<Block> getParentBlocks() {
        return this.parentBlocks;
    }

    public Block parentBlocks(Set<Block> blocks) {
        this.setParentBlocks(blocks);
        return this;
    }

    public Block addParentBlocks(Block block) {
        this.parentBlocks.add(block);
        block.setParent(this);
        return this;
    }

    public Block removeParentBlocks(Block block) {
        this.parentBlocks.remove(block);
        block.setParent(null);
        return this;
    }

    public void setParentBlocks(Set<Block> blocks) {
        if (this.parentBlocks != null) {
            this.parentBlocks.forEach(i -> i.setParent(null));
        }
        if (blocks != null) {
            blocks.forEach(i -> i.setParent(this));
        }
        this.parentBlocks = blocks;
    }

    public Set<FlowBlock> getFlows() {
        return this.flows;
    }

    public Block flows(Set<FlowBlock> flowBlocks) {
        this.setFlows(flowBlocks);
        return this;
    }

    public Block addFlows(FlowBlock flowBlock) {
        this.flows.add(flowBlock);
        flowBlock.setBlock(this);
        return this;
    }

    public Block removeFlows(FlowBlock flowBlock) {
        this.flows.remove(flowBlock);
        flowBlock.setBlock(null);
        return this;
    }

    public void setFlows(Set<FlowBlock> flowBlocks) {
        if (this.flows != null) {
            this.flows.forEach(i -> i.setBlock(null));
        }
        if (flowBlocks != null) {
            flowBlocks.forEach(i -> i.setBlock(this));
        }
        this.flows = flowBlocks;
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

    public static final class Builder {

        private Long id;
        private BlockType type;
        private String content;
        private ZonedDateTime createdDate;
        private String hash;
        private Block parent;
        private User user;
        private Set<Block> parentBlocks = new HashSet<>();
        private Set<FlowBlock> flows = new HashSet<>();

        private Builder() {}

        public static Builder aBlock() {
            return new Builder();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder type(BlockType type) {
            this.type = type;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder createdDate(ZonedDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder hash(String hash) {
            this.hash = hash;
            return this;
        }

        public Builder parent(Block parent) {
            this.parent = parent;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder parentBlocks(Set<Block> parentBlocks) {
            this.parentBlocks = parentBlocks;
            return this;
        }

        public Builder flows(Set<FlowBlock> flows) {
            this.flows = flows;
            return this;
        }

        public Block build() {
            Block block = new Block();
            block.setId(id);
            block.setType(type);
            block.setContent(content);
            block.setCreatedDate(createdDate);
            block.setHash(hash);
            block.setParent(parent);
            block.setUser(user);
            block.setParentBlocks(parentBlocks);
            block.setFlows(flows);
            return block;
        }
    }
}
