package com.pradera.poc.service.mapper;


import com.pradera.poc.domain.*;
import com.pradera.poc.service.dto.BlockDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Block} and its DTO {@link BlockDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BlockMapper extends EntityMapper<BlockDTO, Block> {

    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.content", target = "parentContent")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    BlockDTO toDto(Block block);

    @Mapping(source = "parentId", target = "parent")
    @Mapping(source = "userId", target = "user")
    Block toEntity(BlockDTO blockDTO);

    default Block fromId(Long id) {
        if (id == null) {
            return null;
        }
        Block block = new Block();
        block.setId(id);
        return block;
    }
}
