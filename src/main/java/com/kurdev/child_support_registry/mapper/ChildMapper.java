package com.kurdev.child_support_registry.mapper;

import com.kurdev.child_support_registry.domain.Child;
import com.kurdev.child_support_registry.dto.ChildDto;
import org.mapstruct.*;

@Named("ChildMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {DebtorMapper.class, GuardianMapper.class})
public interface ChildMapper {

    @Mappings({
            @Mapping(target = "debtor", qualifiedByName = {"DebtorMapper", "toDtoWithoutChildren"}),
            @Mapping(target = "guardian", qualifiedByName = {"GuardianMapper", "toDtoWithoutChildren"}),
    })
    ChildDto childToChildDto(Child child);

    Child toEntity(ChildDto dto);

}
