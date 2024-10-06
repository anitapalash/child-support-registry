package com.kurdev.child_support_registry.mapper;

import com.kurdev.child_support_registry.domain.Child;
import com.kurdev.child_support_registry.domain.Guardian;
import com.kurdev.child_support_registry.dto.ChildDto;
import com.kurdev.child_support_registry.dto.GuardianDto;
import com.kurdev.child_support_registry.util.KurdevUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GuardianMapper {

    String IGNORE_GUARDIAN = "ignoreGuardian";

    @Mapping(target = "children", qualifiedByName = IGNORE_GUARDIAN)
    GuardianDto toDto(Guardian guardian);

    Guardian toEntity(GuardianDto dto);

    @Mapping(target = "guardian", ignore = true)
    ChildDto toDtoIgnoreGuardian(Child child);

    @Named(value = IGNORE_GUARDIAN)
    default List<ChildDto> ignoreDebtor (List<Child> children) {
        return KurdevUtils.map(children, this::toDtoIgnoreGuardian);
    }
}
