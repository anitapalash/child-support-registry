package com.kurdev.child_support_registry.mapper;

import com.kurdev.child_support_registry.domain.Child;
import com.kurdev.child_support_registry.dto.ChildDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChildMapper {

    @Mapping(target = "debtor.children", ignore = true)
    @Mapping(target = "guardian.children", ignore = true)
    ChildDto toDto(Child child);

    Child toEntity(ChildDto dto);
}
