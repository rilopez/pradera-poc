package com.pradera.poc.service.mapper;

import com.pradera.poc.domain.*;
import com.pradera.poc.service.dto.FlowDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Flow} and its DTO {@link FlowDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, BookMapper.class, BlockMapper.class })
public interface FlowMapper extends EntityMapper<FlowDTO, Flow> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "book", source = "book", qualifiedByName = "title")
    @Mapping(target = "blocks", source = "blocks", qualifiedByName = "contentSet")
    FlowDTO toDto(Flow s);

    @Mapping(target = "removeBlocks", ignore = true)
    Flow toEntity(FlowDTO flowDTO);
}
