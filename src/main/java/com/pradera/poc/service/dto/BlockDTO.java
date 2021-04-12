package com.pradera.poc.service.dto;

import com.pradera.poc.domain.enumeration.BlockType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.pradera.poc.domain.Block} entity.
 */
public class BlockDTO implements Serializable {

    private Long id;

    @NotNull
    private BlockType type;

    @Lob
    private String content;

    @NotNull
    private ZonedDateTime createdDate;

    @NotNull
    private String hash;

    private BlockDTO parent;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public BlockDTO getParent() {
        return parent;
    }

    public void setParent(BlockDTO parent) {
        this.parent = parent;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlockDTO)) {
            return false;
        }

        BlockDTO blockDTO = (BlockDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, blockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BlockDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", content='" + getContent() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", hash='" + getHash() + "'" +
            ", parent=" + getParent() +
            ", user=" + getUser() +
            "}";
    }
}
