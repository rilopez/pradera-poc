package com.pradera.poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FlowBlock.
 */
@Entity
@Table(name = "flow_block")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FlowBlock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "block_order", nullable = false)
    private Long blockOrder;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "book", "blocks" }, allowSetters = true)
    private Flow flow;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parent", "user", "parentBlocks", "flows" }, allowSetters = true)
    private Block block;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlowBlock id(Long id) {
        this.id = id;
        return this;
    }

    public Long getBlockOrder() {
        return this.blockOrder;
    }

    public FlowBlock blockOrder(Long blockOrder) {
        this.blockOrder = blockOrder;
        return this;
    }

    public void setBlockOrder(Long blockOrder) {
        this.blockOrder = blockOrder;
    }

    public Flow getFlow() {
        return this.flow;
    }

    public FlowBlock flow(Flow flow) {
        this.setFlow(flow);
        return this;
    }

    public void setFlow(Flow flow) {
        this.flow = flow;
    }

    public Block getBlock() {
        return this.block;
    }

    public FlowBlock block(Block block) {
        this.setBlock(block);
        return this;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlowBlock)) {
            return false;
        }
        return id != null && id.equals(((FlowBlock) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlowBlock{" +
            "id=" + getId() +
            ", blockOrder=" + getBlockOrder() +
            "}";
    }
}
