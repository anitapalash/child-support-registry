package com.kurdev.child_support_registry.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GuardianDto {
    @JsonIgnore
    private Long id;
    @NotBlank
    private String surname;
    @NotBlank
    private String name;
    private String patronymic;
    private LocalDate dateOfBirth;

    private List<ChildDto> children;
    private String passport;
    @NotBlank
    private String inn;
    private String registrationAddress;
    private String actualAddress;
    private boolean deleted;
}
