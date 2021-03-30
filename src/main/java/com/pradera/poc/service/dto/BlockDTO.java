package com.pradera.poc.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import com.pradera.poc.domain.enumeration.BlockType;

/**
 * A DTO for the {@link com.pradera.poc.domain.Block} entity.
 */
public class BlockDTO implements Serializable {
    
    private Long id;

    @NotNull
    private BlockType type;

    @NotNull
    private String content;

    @NotNull
    private ZonedDateTime createdDate;

    @NotNull
    private String hash;


    private Long parentId;

    private String parentContent;

    private Long userId;

    private String userLogin;
    
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long blockId) {
        this.parentId = blockId;
    }

    public String getParentContent() {
        return parentContent;
    }

    public void setParentContent(String blockContent) {
        this.parentContent = blockContent;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlockDTO)) {
            return false;
        }

        return id != null && id.equals(((BlockDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
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
            ", parentId=" + getParentId() +
            ", parentContent='" + getParentContent() + "'" +
            ", userId=" + getUserId() +
            ", userLogin='" + getUserLogin() + "'" +
            "}";
    }
}
