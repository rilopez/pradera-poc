package com.pradera.poc.service.mapper;

import com.pradera.poc.domain.*;
import com.pradera.poc.service.dto.BlockDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Block} and its DTO {@link BlockDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface BlockMapper extends EntityMapper<BlockDTO, Block> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "content")
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    BlockDTO toDto(Block s);

    @Named("content")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "content", source = "content")
    BlockDTO toDtoContent(Block block);

    @Named("contentSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "content", source = "content")
    Set<BlockDTO> toDtoContentSet(Set<Block> block);
}
