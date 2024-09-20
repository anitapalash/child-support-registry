package com.kurdev.child_support_registry.service.impl;

import com.kurdev.child_support_registry.domain.Child;
import com.kurdev.child_support_registry.dto.ChildDto;
import com.kurdev.child_support_registry.mapper.ChildMapper;
import com.kurdev.child_support_registry.repository.ChildrenRepository;
import com.kurdev.child_support_registry.service.ChildrenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildrenServiceImpl implements ChildrenService {

    private final ChildrenRepository childrenRepository;
    private final ChildMapper mapper;

    @Override
    public List<ChildDto> findByDebtorId(Long debtorId) {
        return childrenRepository.findByDebtorId(debtorId).stream()
                .map(mapper::childToChildDto).collect(Collectors.toList());
    }

    @Override
    public List<ChildDto> findByGuardianId(Long guardianId) {
        return childrenRepository.findByGuardianId(guardianId).stream()
                .map(mapper::childToChildDto).collect(Collectors.toList());
    }

    @Override
    public Page<ChildDto> getSomeChildren(Pageable pageable) {
        return null;
    }

    @Override
    public List<ChildDto> getAll() {
        return childrenRepository.findAll().stream()
                .map(mapper::childToChildDto).collect(Collectors.toList());
    }

    @Override
    public Page<ChildDto> getPage(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<ChildDto> findById(Long id) {
        return childrenRepository.findById(id).map(mapper::childToChildDto);
    }

    @Override
    public Child create(ChildDto childDto) {
        return childrenRepository.save(mapper.toEntity(childDto));
    }

    @Override
    public List<Child> createList(List<ChildDto> childDtos) {
        return childrenRepository.saveAll(childDtos.stream().map(mapper::toEntity).collect(Collectors.toList()));
    }

    @Override
    public Child update(ChildDto childDto) {
        return childrenRepository.save(mapper.toEntity(childDto));
    }

    @Override
    public void delete(Long id) {
        childrenRepository.delete(id);
    }
}
