package com.pradera.poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pradera.poc.domain.enumeration.BlockType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Flow.
 */
@Entity
@Table(name = "flow")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Flow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    @OneToMany(mappedBy = "flow")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "flow", "block" }, allowSetters = true)
    private Set<FlowBlock> blocks = new HashSet<>();

    public Flow() {}

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flow id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Flow name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return this.user;
    }

    public Flow user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return this.book;
    }

    public Flow book(Book book) {
        this.setBook(book);
        return this;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Set<FlowBlock> getBlocks() {
        return this.blocks;
    }

    public Flow blocks(Set<FlowBlock> flowBlocks) {
        this.setBlocks(flowBlocks);
        return this;
    }

    public Flow addBlocks(Block block) {
        FlowBlock flowBlock = FlowBlock.Builder.aFlowBlock().block(block).build();

        this.addBlocks(flowBlock);
        return this;
    }

    public Flow addBlocks(FlowBlock flowBlock) {
        this.blocks.add(flowBlock);
        flowBlock.setBlockOrder(Long.valueOf(this.blocks.size()));
        flowBlock.setFlow(this);
        return this;
    }

    public Flow removeBlocks(FlowBlock flowBlock) {
        this.blocks.remove(flowBlock);
        flowBlock.setFlow(null);
        return this;
    }

    public void setBlocks(Set<FlowBlock> flowBlocks) {
        if (this.blocks != null) {
            this.blocks.forEach(i -> i.setFlow(null));
        }
        if (flowBlocks != null) {
            flowBlocks.forEach(i -> i.setFlow(this));
        }
        this.blocks = flowBlocks;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Flow)) {
            return false;
        }
        return id != null && id.equals(((Flow) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        getBlocks().forEach(flowBlock -> {
            Block block = flowBlock.getBlock();
            if (block.getType() == BlockType.TITLE) {
                sb.append("# ");
            }
            sb.append(block.getContent()).append("\n");
        });


        return sb.toString();
    }

    public static final class Builder {

        private Long id;
        private String name;
        private User user;
        private Book book;
        private Set<FlowBlock> blocks = new HashSet<>();

        private Builder() {}

        public static Builder aFlow() {
            return new Builder();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder book(Book book) {
            this.book = book;
            return this;
        }

        public Builder blocks(Set<FlowBlock> blocks) {
            this.blocks = blocks;
            return this;
        }

        public Flow build() {
            Flow flow = new Flow();
            flow.setId(id);
            flow.setName(name);
            flow.setUser(user);
            flow.setBook(book);
            flow.setBlocks(blocks);
            return flow;
        }
    }
}
