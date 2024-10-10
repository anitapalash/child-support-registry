package com.kurdev.child_support_registry.controller;

import com.kurdev.child_support_registry.dto.GuardianDto;
import com.kurdev.child_support_registry.service.GuardianService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/guardian", produces = "application/json; charset=utf-8")
//Распростроненная проблема для Swagger3 слетает кодировка
public class GuardianController {

    private final GuardianService service;

    @GetMapping
    public Page<GuardianDto> getSomeDebtors(Pageable pageable) {
        return service.getPage(pageable);
    }

    @GetMapping("/by-child/{id}")
    public Optional<GuardianDto> getChildrenByDebtor(@PathVariable("id") Long childId) {
        return service.findByChildId(childId);
    }

    @PostMapping
    public List<GuardianDto> saveChildren(@RequestBody List<GuardianDto> guardianDtos) {
        return service.create(guardianDtos);
    }

    @PostMapping("/single")
    public GuardianDto saveOneChild(@RequestBody GuardianDto guardianDto) {
        return service.create(List.of(guardianDto)).get(0);
    }
}
