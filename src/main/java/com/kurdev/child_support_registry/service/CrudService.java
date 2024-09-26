package com.kurdev.child_support_registry.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, DTO> {

    List<DTO> getAll();

    Page<DTO> getPage(Pageable pageable);

    Optional<DTO> findById(Long id);

    List<T> create(List<DTO> dtoList);

    T update(DTO dto);

    void delete(Long id);
}
