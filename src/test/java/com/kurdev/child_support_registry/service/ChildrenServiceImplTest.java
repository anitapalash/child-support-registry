package com.kurdev.child_support_registry.service;

import com.kurdev.child_support_registry.domain.Child;
import com.kurdev.child_support_registry.mapper.ChildMapper;
import com.kurdev.child_support_registry.repository.ChildrenRepository;
import com.kurdev.child_support_registry.repository.DebtorsRepository;
import com.kurdev.child_support_registry.repository.GuardiansRepository;
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
public class ChildrenServiceImplTest {
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
    private ChildrenService childrenService;
    @Autowired
    private ChildrenRepository childrenRepository;
    @Autowired
    private DebtorsRepository debtorsRepository;
    @Autowired
    private GuardiansRepository guardiansRepository;
    @Autowired
    private ChildMapper childMapper;

    @BeforeEach
    void setUp() {
        childrenRepository.deleteAll();
    }

    @Test
    void testGetByDebtor() {
        var child = TestConstants.stubChild();
        var debtor = TestConstants.stubDebtor();
        debtor = debtorsRepository.save(debtor);
        child.setDebtor(debtor);
        childrenRepository.save(child);

        assertNotNull(debtor.getId());
        var result = childrenService.findByDebtorId(debtor.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetByNonExistingDebtor() {
        var result = childrenService.findByDebtorId(TestConstants.TEST_ID);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetByGuardian() {
        var child = TestConstants.stubChild();
        var guardian = TestConstants.stubGuardian();
        guardian = guardiansRepository.save(guardian);
        child.setGuardian(guardian);
        childrenRepository.save(child);

        assertNotNull(guardian.getId());
        var result = childrenService.findByGuardianId(guardian.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetByNonExistingGuardian() {
        var result = childrenService.findByGuardianId(TestConstants.TEST_ID);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetPage() {
        var child = TestConstants.stubChild();
        childrenRepository.save(child);
        var result = childrenService.getPage(PageRequest.of(0, 5));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetAll() {
        var child = TestConstants.stubChild();
        childrenRepository.save(child);

        var result = childrenService.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreate() {
        var childDto = TestConstants.stubChildDto();
        var result = childrenService.create(List.of(childDto));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertNotNull(result.get(0).getId());
    }

    @Test
    void testUpdate() {
        var child = TestConstants.stubChild();
        child = childrenRepository.save(child);
        String nameBefore = child.getName();

        child.setName("Константин");
        var newChild = childMapper.toDto(child);

        var result = childrenService.update(newChild);

        assertNotNull(result);
        assertEquals(child.getId(), result.getId());
        assertNotEquals(nameBefore, result.getName());
    }

    @Test
    void testFindById() {
        var child = TestConstants.stubChild();
        child = childrenRepository.save(child);

        assertNotNull(child.getId());

        var result = childrenService.findById(child.getId());

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(child.getSurname(), result.get().getSurname());
        assertEquals(child.getName(), result.get().getName());
        assertEquals(child.getDateOfBirth(), result.get().getDateOfBirth());
    }

    @Test
    void testFindByNonExistingId() {
        var result = childrenService.findById(TestConstants.TEST_ID);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDelete() {
        var child = TestConstants.stubChild();
        child = childrenRepository.save(child);
        assertNotNull(child.getId());
        Child finalChild = child;

        var savedChild = childrenRepository.findById(finalChild.getId());
        assertTrue(savedChild.isPresent());
        assertFalse(savedChild.get().isDeleted());
        assertDoesNotThrow(() -> childrenService.delete(finalChild.getId()));
        savedChild = childrenRepository.findById(finalChild.getId());
        assertTrue(savedChild.isPresent());
        assertTrue(savedChild.get().isDeleted());
    }
}
