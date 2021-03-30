package com.pradera.poc.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.pradera.poc.domain.enumeration.BlockType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

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

        public BlockTypeFilter() {
        }

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

    private StringFilter content;

    private ZonedDateTimeFilter createdDate;

    private StringFilter hash;

    private LongFilter parentId;

    private LongFilter userId;

    public BlockCriteria() {
    }

    public BlockCriteria(BlockCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.createdDate = other.createdDate == null ? null : other.createdDate.copy();
        this.hash = other.hash == null ? null : other.hash.copy();
        this.parentId = other.parentId == null ? null : other.parentId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public BlockCriteria copy() {
        return new BlockCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BlockTypeFilter getType() {
        return type;
    }

    public void setType(BlockTypeFilter type) {
        this.type = type;
    }

    public StringFilter getContent() {
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getHash() {
        return hash;
    }

    public void setHash(StringFilter hash) {
        this.hash = hash;
    }

    public LongFilter getParentId() {
        return parentId;
    }

    public void setParentId(LongFilter parentId) {
        this.parentId = parentId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(content, that.content) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(hash, that.hash) &&
            Objects.equals(parentId, that.parentId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        content,
        createdDate,
        hash,
        parentId,
        userId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlockCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (content != null ? "content=" + content + ", " : "") +
                (createdDate != null ? "createdDate=" + createdDate + ", " : "") +
                (hash != null ? "hash=" + hash + ", " : "") +
                (parentId != null ? "parentId=" + parentId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
