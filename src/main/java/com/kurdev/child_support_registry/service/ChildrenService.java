package com.kurdev.child_support_registry.service;

import com.kurdev.child_support_registry.domain.Child;
import com.kurdev.child_support_registry.dto.ChildDto;

import java.util.List;

public interface ChildrenService extends CrudService<Child, ChildDto> {

    List<ChildDto> findByDebtorId(Long debtorId);

    List<ChildDto> findByGuardianId(Long guardianId);
}
