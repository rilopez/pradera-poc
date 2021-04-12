package com.pradera.poc.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pradera.poc.domain.FlowBlock} entity.
 */
public class FlowBlockDTO implements Serializable {

    private Long id;

    @NotNull
    private Long blockOrder;

    private FlowDTO flow;

    private BlockDTO block;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlockOrder() {
        return blockOrder;
    }

    public void setBlockOrder(Long blockOrder) {
        this.blockOrder = blockOrder;
    }

    public FlowDTO getFlow() {
        return flow;
    }

    public void setFlow(FlowDTO flow) {
        this.flow = flow;
    }

    public BlockDTO getBlock() {
        return block;
    }

    public void setBlock(BlockDTO block) {
        this.block = block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlowBlockDTO)) {
            return false;
        }

        FlowBlockDTO flowBlockDTO = (FlowBlockDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, flowBlockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlowBlockDTO{" +
            "id=" + getId() +
            ", blockOrder=" + getBlockOrder() +
            ", flow=" + getFlow() +
            ", block=" + getBlock() +
            "}";
    }
}
