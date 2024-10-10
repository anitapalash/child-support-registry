package com.kurdev.child_support_registry.mapper;

import com.kurdev.child_support_registry.domain.Child;
import com.kurdev.child_support_registry.domain.Debtor;
import com.kurdev.child_support_registry.dto.ChildDto;
import com.kurdev.child_support_registry.dto.DebtorDto;
import com.kurdev.child_support_registry.util.KurdevUtils;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DebtorMapper {
    String IGNORE_DEBTOR = "ignoreDebtor";

    @Mapping(target = "children", qualifiedByName = IGNORE_DEBTOR)
    DebtorDto toDto(Debtor debtor);

    @Mapping(target = "debtor", ignore = true)
    ChildDto toDtoIgnoreDebtor(Child child);

    @Named(value = IGNORE_DEBTOR)
    default List<ChildDto> ignoreDebtor (List<Child> children) {
        return KurdevUtils.map(children, this::toDtoIgnoreDebtor);
    }

    Debtor toEntity(DebtorDto dto);
}
