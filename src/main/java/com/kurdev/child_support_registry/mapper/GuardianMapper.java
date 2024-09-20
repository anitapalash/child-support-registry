package com.kurdev.child_support_registry.mapper;

import com.kurdev.child_support_registry.domain.Guardian;
import com.kurdev.child_support_registry.dto.GuardianDto;
import org.mapstruct.*;

@Named("GuardianMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GuardianMapper {

    GuardianDto guardianToDto(Guardian guardian);

    @Named("toDtoWithoutChildren")
    @Mappings({
            @Mapping(target = "children", expression = "java(null)")})
    GuardianDto guardianToDtoWithoutChildren(Guardian guardian);

    Guardian toEntity(GuardianDto dto);
}
