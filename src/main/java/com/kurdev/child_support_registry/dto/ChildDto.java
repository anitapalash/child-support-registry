package com.kurdev.child_support_registry.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ChildDto implements Serializable {

    @NotBlank
    private String surname;
    @NotBlank
    private String name;
    private String patronymic;
    private LocalDate dateOfBirth;
    private Long debt;

    private DebtorDto debtor;
    private GuardianDto guardian;
    private boolean deleted = false;
}
