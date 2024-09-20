package com.kurdev.child_support_registry.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.ZonedDateTime;
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
    private ZonedDateTime dateOfBirth;

    private List<ChildDto> children;
    private String passport;
    @NotBlank
    private String inn;
    private String registrationAddress;
    private String actualAddress;
    private boolean deleted;
}
