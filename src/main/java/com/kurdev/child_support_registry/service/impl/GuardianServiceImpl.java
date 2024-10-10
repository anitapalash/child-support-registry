package com.kurdev.child_support_registry.service.impl;

import com.kurdev.child_support_registry.domain.Guardian;
import com.kurdev.child_support_registry.dto.GuardianDto;
import com.kurdev.child_support_registry.mapper.GuardianMapper;
import com.kurdev.child_support_registry.repository.GuardiansRepository;
import com.kurdev.child_support_registry.service.GuardianService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class GuardianServiceImpl implements GuardianService {

    private final GuardiansRepository guardiansRepository;
    private final GuardianMapper mapper;

    @Override
    public List<GuardianDto> getAll() {
        return guardiansRepository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<GuardianDto> getPage(Pageable pageable) {
        return guardiansRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Optional<GuardianDto> findById(Long id) {
        return guardiansRepository.findById(id).map(mapper::toDto);
    }

    @Override
    public List<GuardianDto> create(List<GuardianDto> guardianDtos) {
        return guardiansRepository.saveAll(guardianDtos.stream().map(mapper::toEntity).collect(Collectors.toList())).stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Guardian update(GuardianDto guardianDto) {
        return guardiansRepository.save(mapper.toEntity(guardianDto));
    }

    @Override
    public void delete(Long id) {
        guardiansRepository.delete(id);
    }

    @Override
    public Optional<GuardianDto> findByChildId(Long childId) {
        return guardiansRepository.findByChildId(childId).map(mapper::toDto);
    }
}
