package com.kurdev.child_support_registry.controller;

import com.kurdev.child_support_registry.domain.Child;
import com.kurdev.child_support_registry.dto.ChildDto;
import com.kurdev.child_support_registry.service.ChildrenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/children", produces = "application/json; charset=utf-8")
//Распростроненная проблема для Swagger3 слетает кодировка
public class ChildrenController {

    private final ChildrenService service;

    @GetMapping
    public Page<ChildDto> getSomeChildren(Pageable pageable) {
        return service.getPage(pageable);
    }

    @GetMapping("/by-debtor/{id}")
    public List<ChildDto> getChildrenByDebtor(@PathVariable("id") Long debtorId) {
        return service.findByDebtorId(debtorId);
    }

    @GetMapping("/by-guardian/{id}")
    public List<ChildDto> getChildrenByGuardian(@PathVariable("id") Long guardianId) {
        return service.findByGuardianId(guardianId);
    }

    @PostMapping
    public List<Child> saveChildren(@RequestBody List<ChildDto> childDtos) {
        return service.create(childDtos);
    }

    @PostMapping("/single")
    public Child saveOneChild(@RequestBody ChildDto childDto) {
        return service.create(List.of(childDto)).get(0);
    }
}
