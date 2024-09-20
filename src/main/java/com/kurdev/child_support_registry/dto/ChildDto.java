package com.kurdev.child_support_registry.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ChildDto {

    @NotBlank
    private String surname;
    @NotBlank
    private String name;
    private String patronymic;
    private ZonedDateTime dateOfBirth;
    private Long debt;

    @NotNull
    private DebtorDto debtor;
    private GuardianDto guardian;
    private boolean deleted;
}
