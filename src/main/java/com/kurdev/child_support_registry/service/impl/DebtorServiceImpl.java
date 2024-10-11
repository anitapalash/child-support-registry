package com.kurdev.child_support_registry.service.impl;

import com.kurdev.child_support_registry.domain.Debtor;
import com.kurdev.child_support_registry.dto.DebtorDto;
import com.kurdev.child_support_registry.mapper.DebtorMapper;
import com.kurdev.child_support_registry.repository.DebtorsRepository;
import com.kurdev.child_support_registry.service.DebtorService;
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
public class DebtorServiceImpl implements DebtorService {

    private final DebtorsRepository debtorsRepository;
    private final DebtorMapper mapper;

    @Override
    public List<DebtorDto> getAll() {
        return debtorsRepository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<DebtorDto> getPage(Pageable pageable) {
        return debtorsRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Optional<DebtorDto> findById(Long id) {
        return debtorsRepository.findById(id).map(mapper::toDto);
    }

    @Override
    public List<DebtorDto> create(List<DebtorDto> debtorDtos) {
        return debtorsRepository.saveAll(debtorDtos.stream().map(mapper::toEntity).collect(Collectors.toList())).stream()
                .map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Debtor update(DebtorDto dto) {
        return debtorsRepository.save(mapper.toEntity(dto));
    }

    @Override
    public void delete(Long id) {
        debtorsRepository.delete(id);
    }

    @Override
    public Optional<DebtorDto> findByChildId(Long childId) {
        return debtorsRepository.findByChildId(childId).map(mapper::toDto);
    }
}
