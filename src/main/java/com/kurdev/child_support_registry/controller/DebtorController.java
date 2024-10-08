package com.kurdev.child_support_registry.controller;

import com.kurdev.child_support_registry.dto.DebtorDto;
import com.kurdev.child_support_registry.service.DebtorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/debtor", produces = "application/json; charset=utf-8")
//Распростроненная проблема для Swagger3 слетает кодировка
public class DebtorController {

    private final DebtorService service;

    @GetMapping
    public Page<DebtorDto> getSomeDebtors(Pageable pageable) {
        return service.getPage(pageable);
    }

    @GetMapping("/by-child/{id}")
    public Optional<DebtorDto> getChildrenByDebtor(@PathVariable("id") Long childId) {
        return service.findByChildId(childId);
    }

    @PostMapping
    public List<DebtorDto> saveChildren(@RequestBody List<DebtorDto> debtorDtos) {
        return service.create(debtorDtos);
    }

    @PostMapping("/single")
    public DebtorDto saveOneChild(@RequestBody DebtorDto debtorDto) {
        return service.create(List.of(debtorDto)).get(0);
    }
}
