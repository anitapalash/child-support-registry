package com.kurdev.child_support_registry.stub;

import com.kurdev.child_support_registry.domain.Child;
import com.kurdev.child_support_registry.dto.ChildDto;
import com.kurdev.child_support_registry.dto.DebtorDto;
import com.kurdev.child_support_registry.dto.GuardianDto;

public class TestConstants {

    public static final Long TEST_ID = 1L;

    public static ChildDto stubChildDto() {
        ChildDto childDto = new ChildDto();
        childDto.setSurname("Иванов");
        childDto.setName("Игорь");
        childDto.setPatronymic("Иванович");
        childDto.setDebt(150_000L);

        return childDto;
    }

    public static Child stubChild() {
        Child child = new Child();
        child.setId(TEST_ID);
        child.setSurname("Иванов");
        child.setName("Игорь");
        child.setPatronymic("Иванович");
        child.setDebt(150_000L);

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

        return dto;
    }

    public static GuardianDto stubGuardianDto() {
        GuardianDto dto = new GuardianDto();
        dto.setId(TEST_ID);
        dto.setSurname("Селезнева");
        dto.setName("Елена");
        dto.setPatronymic("Дмитриевна");
        dto.setPassport("1234 222222");
        dto.setInn("198765432109");

        return dto;
    }
}
