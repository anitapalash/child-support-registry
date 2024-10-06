package com.kurdev.child_support_registry.service;

import com.kurdev.child_support_registry.domain.Guardian;
import com.kurdev.child_support_registry.mapper.GuardianMapper;
import com.kurdev.child_support_registry.repository.ChildrenRepository;
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
public class GuardianServiceImplTest {
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
    private GuardianService guardianService;
    @Autowired
    private ChildrenRepository childrenRepository;
    @Autowired
    private GuardiansRepository guardiansRepository;
    @Autowired
    private GuardianMapper guardianMapper;

    @BeforeEach
    void setUp() {
        guardiansRepository.deleteAll();
    }

    @Test
    void testGetByChild() {
        var guardian = TestConstants.stubGuardian();
        var child = TestConstants.stubChild();
        child = childrenRepository.save(child);
        guardian.setChildren(List.of(child));
        guardian = guardiansRepository.save(guardian);

        assertNotNull(child.getId());
        var result = guardianService.findByChildId(child.getId());
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(guardian.getId(), result.get().getId());
    }

    @Test
    void testGetByNonExistingGuardian() {
        var result = guardianService.findByChildId(TestConstants.TEST_ID);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPage() {
        var guardian = TestConstants.stubGuardian();
        guardiansRepository.save(guardian);
        var result = guardianService.getPage(PageRequest.of(0, 5));
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetAll() {
        var guardian = TestConstants.stubGuardian();
        guardiansRepository.save(guardian);

        var result = guardianService.getAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreate() {
        var guardianDto = TestConstants.stubGuardianDto();
        var result = guardianService.create(List.of(guardianDto));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertNotNull(result.get(0).getId());
    }

    @Test
    void testUpdate() {
        var guardian = TestConstants.stubGuardian();
        guardian = guardiansRepository.save(guardian);
        String nameBefore = guardian.getName();

        guardian.setName("Константин");
        var newGuardian = guardianMapper.toDto(guardian);

        var result = guardianService.update(newGuardian);

        assertNotNull(result);
        assertEquals(guardian.getId(), result.getId());
        assertNotEquals(nameBefore, result.getName());
    }

    @Test
    void testFindById() {
        var guardian = TestConstants.stubGuardian();
        guardian = guardiansRepository.save(guardian);

        assertNotNull(guardian.getId());

        var result = guardianService.findById(guardian.getId());

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(guardian.getSurname(), result.get().getSurname());
        assertEquals(guardian.getName(), result.get().getName());
        assertEquals(guardian.getDateOfBirth(), result.get().getDateOfBirth());
    }

    @Test
    void testFindByNonExistingId() {
        var result = guardianService.findById(TestConstants.TEST_ID);
        assertTrue(result.isEmpty());
    }

    @Test
    void testDelete() {
        var guardian = TestConstants.stubGuardian();
        guardian = guardiansRepository.save(guardian);
        assertNotNull(guardian.getId());
        Guardian finalGuardian = guardian;

        var savedChild = guardiansRepository.findById(finalGuardian.getId());
        assertTrue(savedChild.isPresent());
        assertFalse(savedChild.get().isDeleted());
        assertDoesNotThrow(() -> guardianService.delete(finalGuardian.getId()));
        savedChild = guardiansRepository.findById(finalGuardian.getId());
        assertTrue(savedChild.isPresent());
        assertTrue(savedChild.get().isDeleted());
    }
}
