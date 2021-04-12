package com.pradera.poc.service.mapper;

import com.pradera.poc.domain.*;
import com.pradera.poc.service.dto.FlowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Flow} and its DTO {@link FlowDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, BookMapper.class })
public interface FlowMapper extends EntityMapper<FlowDTO, Flow> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "book", source = "book", qualifiedByName = "title")
    FlowDTO toDto(Flow s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FlowDTO toDtoId(Flow flow);
}
