package com.pradera.poc.service.mapper;

import com.pradera.poc.domain.*;
import com.pradera.poc.service.dto.FlowBlockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FlowBlock} and its DTO {@link FlowBlockDTO}.
 */
@Mapper(componentModel = "spring", uses = { FlowMapper.class, BlockMapper.class })
public interface FlowBlockMapper extends EntityMapper<FlowBlockDTO, FlowBlock> {
    @Mapping(target = "flow", source = "flow", qualifiedByName = "id")
    @Mapping(target = "block", source = "block", qualifiedByName = "id")
    FlowBlockDTO toDto(FlowBlock s);
}
