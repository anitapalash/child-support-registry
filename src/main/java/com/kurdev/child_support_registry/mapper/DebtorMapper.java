package com.kurdev.child_support_registry.mapper;

import com.kurdev.child_support_registry.domain.Debtor;
import com.kurdev.child_support_registry.dto.DebtorDto;
import org.mapstruct.*;

@Named("DebtorMapper")
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DebtorMapper {

    DebtorDto debtorToDto(Debtor debtor);

    @Named("toDtoWithoutChildren")
    @Mappings({
            @Mapping(target = "children", expression = "java(null)")})
    DebtorDto debtorToDtoWithoutChildren(Debtor debtor);

    Debtor toEntity(DebtorDto dto);
}
