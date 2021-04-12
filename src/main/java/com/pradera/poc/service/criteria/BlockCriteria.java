package com.pradera.poc.service.criteria;

import com.pradera.poc.domain.enumeration.BlockType;
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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.pradera.poc.domain.Block} entity. This class is used
 * in {@link com.pradera.poc.web.rest.BlockResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /blocks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BlockCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BlockType
     */
    public static class BlockTypeFilter extends Filter<BlockType> {

        public BlockTypeFilter() {}

        public BlockTypeFilter(BlockTypeFilter filter) {
            super(filter);
        }

        @Override
        public BlockTypeFilter copy() {
            return new BlockTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BlockTypeFilter type;

    private ZonedDateTimeFilter createdDate;

    private StringFilter hash;

    private LongFilter parentId;

    private LongFilter userId;

    private LongFilter parentBlocksId;

    private LongFilter flowsId;

    public BlockCriteria() {}

    public BlockCriteria(BlockCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.hash = other.hash == null ? null : other.hash.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.parentBlocksId = other.parentBlocksId == null ? null : other.parentBlocksId.copy();
        this.flowsId = other.flowsId == null ? null : other.flowsId.copy();
    }

    @Override
    public BlockCriteria copy() {
        return new BlockCriteria(this);
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

    public BlockTypeFilter getType() {
        return type;
    }

    public BlockTypeFilter type() {
        if (type == null) {
            type = new BlockTypeFilter();
        }
        return type;
    }

    public void setType(BlockTypeFilter type) {
        this.type = type;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            createdDate = new ZonedDateTimeFilter();
        }
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getHash() {
        return hash;
    }

    public StringFilter hash() {
        if (hash == null) {
            hash = new StringFilter();
        }
        return hash;
    }

    public void setHash(StringFilter hash) {
        this.hash = hash;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public LongFilter parentId() {
        if (parentId == null) {
            parentId = new LongFilter();
        }
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getParentBlocksId() {
        return parentBlocksId;
    }

    public LongFilter parentBlocksId() {
        if (parentBlocksId == null) {
            parentBlocksId = new LongFilter();
        }
        return parentBlocksId;
    }

    public void setParentBlocksId(LongFilter parentBlocksId) {
        this.parentBlocksId = parentBlocksId;
    }

    public LongFilter getFlowsId() {
        return flowsId;
    }

    public LongFilter flowsId() {
        if (flowsId == null) {
            flowsId = new LongFilter();
        }
        return flowsId;
    }

    public void setFlowsId(LongFilter flowsId) {
        this.flowsId = flowsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BlockCriteria that = (BlockCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(hash, that.hash) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(parentBlocksId, that.parentBlocksId) &&
            Objects.equals(flowsId, that.flowsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, createdDate, hash, parentId, userId, parentBlocksId, flowsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlockCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
            (hash != null ? "hash=" + hash + ", " : "") +
            (parentId != null ? "parentId=" + parentId + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (parentBlocksId != null ? "parentBlocksId=" + parentBlocksId + ", " : "") +
            (flowsId != null ? "flowsId=" + flowsId + ", " : "") +
            "}";
    }
}
