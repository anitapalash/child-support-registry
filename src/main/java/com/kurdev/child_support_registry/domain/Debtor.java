package com.kurdev.child_support_registry.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "debtors")
@Getter
@Setter
public class Debtor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String surname;
    @Column
    private String name;
    @Column
    private String patronymic;
    @Column
    private LocalDate dateOfBirth;

    @OneToMany
    @JoinColumn(name = "debtor_id", referencedColumnName = "id")
    private List<Child> children;
    @Column
    private String passport;
    @Column
    private String inn;
    @Column
    private String registrationAddress;
    @Column
    private String actualAddress;
    @Column
    private boolean deleted = false;
}
