package com.pradera.poc.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.pradera.poc.domain.FlowBlock} entity. This class is used
 * in {@link com.pradera.poc.web.rest.FlowBlockResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /flow-blocks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FlowBlockCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter blockOrder;

    private LongFilter flowId;

    private LongFilter blockId;

    public FlowBlockCriteria() {}

    public FlowBlockCriteria(FlowBlockCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.blockOrder = other.blockOrder == null ? null : other.blockOrder.copy();
        this.flowId = other.flowId == null ? null : other.flowId.copy();
        this.blockId = other.blockId == null ? null : other.blockId.copy();
    }

    @Override
    public FlowBlockCriteria copy() {
        return new FlowBlockCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getBlockOrder() {
        return blockOrder;
    }

    public LongFilter blockOrder() {
        if (blockOrder == null) {
            blockOrder = new LongFilter();
        }
        return blockOrder;
    }

    public void setBlockOrder(LongFilter blockOrder) {
        this.blockOrder = blockOrder;
    }

    public LongFilter getFlowId() {
        return flowId;
    }

    public LongFilter flowId() {
        if (flowId == null) {
            flowId = new LongFilter();
        }
        return flowId;
    }

    public void setFlowId(LongFilter flowId) {
        this.flowId = flowId;
    }

    public LongFilter getBlockId() {
        return blockId;
    }

    public LongFilter blockId() {
        if (blockId == null) {
            blockId = new LongFilter();
        }
        return blockId;
    }

    public void setBlockId(LongFilter blockId) {
        this.blockId = blockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FlowBlockCriteria that = (FlowBlockCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(blockOrder, that.blockOrder) &&
            Objects.equals(flowId, that.flowId) &&
            Objects.equals(blockId, that.blockId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, blockOrder, flowId, blockId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlowBlockCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (blockOrder != null ? "blockOrder=" + blockOrder + ", " : "") +
            (flowId != null ? "flowId=" + flowId + ", " : "") +
            (blockId != null ? "blockId=" + blockId + ", " : "") +
            "}";
    }
}
