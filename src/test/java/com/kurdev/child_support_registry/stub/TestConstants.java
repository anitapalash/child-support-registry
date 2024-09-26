package com.kurdev.child_support_registry.stub;

import com.kurdev.child_support_registry.domain.Child;
import com.kurdev.child_support_registry.domain.Debtor;
import com.kurdev.child_support_registry.domain.Guardian;
import com.kurdev.child_support_registry.dto.ChildDto;
import com.kurdev.child_support_registry.dto.DebtorDto;
import com.kurdev.child_support_registry.dto.GuardianDto;

import java.time.LocalDate;

public class TestConstants {

    public static final Long TEST_ID = 1L;

    public static ChildDto stubChildDto() {
        ChildDto childDto = new ChildDto();
        childDto.setSurname("Иванов");
        childDto.setName("Игорь");
        childDto.setPatronymic("Иванович");
        childDto.setDebt(150_000L);
        childDto.setDateOfBirth(LocalDate.of(2020, 5, 15));

        return childDto;
    }

    public static Child stubChild() {
        Child child = new Child();
        child.setId(TEST_ID);
        child.setSurname("Иванов");
        child.setName("Игорь");
        child.setPatronymic("Иванович");
        child.setDebt(150_000L);
        child.setDateOfBirth(LocalDate.of(2020, 5, 15));

        return child;
    }

    public static DebtorDto stubDebtorDto() {
        DebtorDto dto = new DebtorDto();
        dto.setId(TEST_ID);
        dto.setSurname("Иванов");
        dto.setName("Иван");
        dto.setPatronymic("Иванович");
        dto.setPassport("1234 111111");
        dto.setInn("123456789123");
        dto.setDateOfBirth(LocalDate.of(1994, 7, 23));

        return dto;
    }

    public static Debtor stubDebtor() {
        Debtor debtor = new Debtor();
        debtor.setId(TEST_ID);
        debtor.setSurname("Иванов");
        debtor.setName("Иван");
        debtor.setPatronymic("Иванович");
        debtor.setPassport("1234 111111");
        debtor.setInn("123456789123");
        debtor.setDateOfBirth(LocalDate.of(1994, 7, 23));

        return debtor;
    }

    public static GuardianDto stubGuardianDto() {
        GuardianDto dto = new GuardianDto();
        dto.setId(TEST_ID);
        dto.setSurname("Селезнева");
        dto.setName("Елена");
        dto.setPatronymic("Дмитриевна");
        dto.setPassport("1234 222222");
        dto.setInn("198765432109");
        dto.setDateOfBirth(LocalDate.of(1994, 7, 23));

        return dto;
    }

    public static Guardian stubGuardian() {
        Guardian guardian = new Guardian();
        guardian.setId(TEST_ID);
        guardian.setSurname("Селезнева");
        guardian.setName("Елена");
        guardian.setPatronymic("Дмитриевна");
        guardian.setPassport("1234 222222");
        guardian.setInn("198765432109");
        guardian.setDateOfBirth(LocalDate.of(1994, 7, 23));

        return guardian;
    }
}
