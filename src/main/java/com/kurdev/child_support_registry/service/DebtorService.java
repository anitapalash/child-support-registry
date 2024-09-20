package com.kurdev.child_support_registry.service;

import com.kurdev.child_support_registry.domain.Debtor;
import com.kurdev.child_support_registry.dto.DebtorDto;

import java.util.Optional;

public interface DebtorService extends CrudService<Debtor, DebtorDto> {

    Optional<DebtorDto> findByChildId(Long childId);
}
