package com.kurdev.child_support_registry.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "children")
@Getter
@Setter
public class Child {

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
    private ZonedDateTime dateOfBirth;
    @Column
    private Long debt;

    @ManyToOne
    @JoinColumn(name = "debtor_id")
    private Debtor debtor;

    @ManyToOne
    @JoinColumn(name = "guardian_id")
    private Guardian guardian;
    @Column
    private boolean deleted = false;
}
