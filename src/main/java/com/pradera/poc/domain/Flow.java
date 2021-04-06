package com.pradera.poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "rel_flow__blocks", joinColumns = @JoinColumn(name = "flow_id"), inverseJoinColumns = @JoinColumn(name = "blocks_id"))
    @JsonIgnoreProperties(value = { "parent", "user", "flows" }, allowSetters = true)
    private Set<Block> blocks = new HashSet<>();

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

    public Set<Block> getBlocks() {
        return this.blocks;
    }

    public Flow blocks(Set<Block> blocks) {
        this.setBlocks(blocks);
        return this;
    }

    public Flow addBlocks(Block block) {
        this.blocks.add(block);
        block.getFlows().add(this);
        return this;
    }

    public Flow removeBlocks(Block block) {
        this.blocks.remove(block);
        block.getFlows().remove(this);
        return this;
    }

    public void setBlocks(Set<Block> blocks) {
        this.blocks = blocks;
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
        return "Flow{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
