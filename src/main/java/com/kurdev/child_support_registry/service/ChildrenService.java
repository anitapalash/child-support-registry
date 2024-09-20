package com.kurdev.child_support_registry.service;

import com.kurdev.child_support_registry.domain.Child;
import com.kurdev.child_support_registry.dto.ChildDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChildrenService extends CrudService<Child, ChildDto> {

    List<ChildDto> findByDebtorId(Long debtorId);

    List<ChildDto> findByGuardianId(Long guardianId);

    Page<ChildDto> getSomeChildren(Pageable pageable);
}
