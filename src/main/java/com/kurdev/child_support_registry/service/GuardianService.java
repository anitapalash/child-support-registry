package com.kurdev.child_support_registry.service;

import com.kurdev.child_support_registry.domain.Guardian;
import com.kurdev.child_support_registry.dto.GuardianDto;

import java.util.Optional;

public interface GuardianService extends CrudService<Guardian, GuardianDto> {

    Optional<GuardianDto> findByChildId(Long childId);
}
