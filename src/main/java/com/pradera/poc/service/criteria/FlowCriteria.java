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
 * Criteria class for the {@link com.pradera.poc.domain.Flow} entity. This class is used
 * in {@link com.pradera.poc.web.rest.FlowResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /flows?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FlowCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter userId;

    private LongFilter bookId;

    private LongFilter blocksId;

    public FlowCriteria() {}

    public FlowCriteria(FlowCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.bookId = other.bookId == null ? null : other.bookId.copy();
        this.blocksId = other.blocksId == null ? null : other.blocksId.copy();
    }

    @Override
    public FlowCriteria copy() {
        return new FlowCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public LongFilter getBookId() {
        return bookId;
    }

    public LongFilter bookId() {
        if (bookId == null) {
            bookId = new LongFilter();
        }
        return bookId;
    }

    public void setBookId(LongFilter bookId) {
        this.bookId = bookId;
    }

    public LongFilter getBlocksId() {
        return blocksId;
    }

    public LongFilter blocksId() {
        if (blocksId == null) {
            blocksId = new LongFilter();
        }
        return blocksId;
    }

    public void setBlocksId(LongFilter blocksId) {
        this.blocksId = blocksId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FlowCriteria that = (FlowCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(bookId, that.bookId) &&
            Objects.equals(blocksId, that.blocksId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userId, bookId, blocksId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlowCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (bookId != null ? "bookId=" + bookId + ", " : "") +
            (blocksId != null ? "blocksId=" + blocksId + ", " : "") +
            "}";
    }
}
