package com.kurdev.child_support_registry.service;

import com.kurdev.child_support_registry.domain.Debtor;
import com.kurdev.child_support_registry.mapper.DebtorMapper;
import com.kurdev.child_support_registry.repository.ChildrenRepository;
import com.kurdev.child_support_registry.repository.DebtorsRepository;
import com.kurdev.child_support_registry.stub.TestConstants;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DebtorServiceImplTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:13.3"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private DebtorService debtorService;
    @Autowired
    private ChildrenRepository childrenRepository;
    @Autowired
    private DebtorsRepository debtorsRepository;
    @Autowired
    private DebtorMapper debtorMapper;

    @BeforeEach
    void setUp() {
        debtorsRepository.deleteAll();
    }

    @Test
    void testGetByChild() {
        var debtor = TestConstants.stubDebtor();
        var child = TestConstants.stubChild();
        child = childrenRepository.save(child);
        debtor.setChildren(List.of(child));
        debtor = debtorsRepository.save(debtor);

        assertNotNull(child.getId());
        var result = debtorService.findByChildId(child.getId());
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(debtor.getId(), result.get().getId());
    }

    @Test
    void testGetByNonExistingDebtor() {
        var result = debtorService.findByChildId(TestConstants.TEST_ID);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPage() {
        var debtor = TestConstants.stubDebtor();
        debtorsRepository.save(debtor);
        var result = debtorService.getPage(PageRequest.of(0, 5));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetAll() {
        var debtor = TestConstants.stubDebtor();
        debtorsRepository.save(debtor);

        var result = debtorService.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreate() {
        var debtorDto = TestConstants.stubDebtorDto();
        var result = debtorService.create(List.of(debtorDto));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertNotNull(result.get(0).getId());
    }

    @Test
    void testUpdate() {
        var debtor = TestConstants.stubDebtor();
        debtor = debtorsRepository.save(debtor);
        String nameBefore = debtor.getName();

        debtor.setName("Константин");
        var newDebtor = debtorMapper.toDto(debtor);

        var result = debtorService.update(newDebtor);

        assertNotNull(result);
        assertEquals(debtor.getId(), result.getId());
        assertNotEquals(nameBefore, result.getName());
    }

    @Test
    void testFindById() {
        var debtor = TestConstants.stubDebtor();
        debtor = debtorsRepository.save(debtor);

        assertNotNull(debtor.getId());

        var result = debtorService.findById(debtor.getId());

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(debtor.getSurname(), result.get().getSurname());
        assertEquals(debtor.getName(), result.get().getName());
        assertEquals(debtor.getDateOfBirth(), result.get().getDateOfBirth());
    }

    @Test
    void testFindByNonExistingId() {
        var result = debtorService.findById(TestConstants.TEST_ID);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDelete() {
        var debtor = TestConstants.stubDebtor();
        debtor = debtorsRepository.save(debtor);
        assertNotNull(debtor.getId());
        Debtor finalDebtor = debtor;

        var savedChild = debtorsRepository.findById(finalDebtor.getId());
        assertTrue(savedChild.isPresent());
        assertFalse(savedChild.get().isDeleted());
        assertDoesNotThrow(() -> debtorService.delete(finalDebtor.getId()));
        savedChild = debtorsRepository.findById(finalDebtor.getId());
        assertTrue(savedChild.isPresent());
        assertTrue(savedChild.get().isDeleted());
    }
}
