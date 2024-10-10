package com.kurdev.child_support_registry.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.kurdev.child_support_registry.mapper.ChildMapper;
import com.kurdev.child_support_registry.repository.GuardiansRepository;
import com.kurdev.child_support_registry.service.ChildrenService;
import com.kurdev.child_support_registry.service.GuardianService;
import com.kurdev.child_support_registry.stub.TestConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class GuardianControllerTest {
    public static final String GUARDIAN_PATH = "/guardian";
    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.3");

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
    private GuardianService guardianService;
    @Autowired
    private GuardiansRepository guardiansRepository;
    @Autowired
    private ChildMapper childMapper;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        guardiansRepository.deleteAll();
    }

    @Test
    void getEmptyPage() {
        var result = given()
                .when()
                .get(GUARDIAN_PATH);

        assertEquals(HttpStatus.OK.value(), result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().as(JsonNode.class).get("totalElements").asInt());
    }

    @Test
    void getPageTest() {
        var guardian = TestConstants.stubGuardianDto();
        guardianService.create(List.of(guardian));

        var result = given()
                .when()
                .get(GUARDIAN_PATH);

        assertEquals(HttpStatus.OK.value(), result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().as(JsonNode.class).get("totalElements").asInt());
    }

    @Test
    void getDebtorByChildTest() {
        var guardian = TestConstants.stubGuardianDto();
        guardian = guardianService.create(List.of(guardian)).get(0);
        var child = TestConstants.stubChildDto();
        child.setGuardian(guardian);
        var children = childrenService.create(List.of(child));

        given()
                .when()
                .get(GUARDIAN_PATH + "/by-child/" + children.get(0).getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("surname", is("Селезнева"))
                .and()
                .body("name", is("Елена"));
    }

    @Test
    void saveGuardiansTest() {
        var guardianDto = TestConstants.stubGuardianDto();
        var guardianDto2 = TestConstants.stubGuardianDto();
        guardianDto2.setName("Светлана");

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(List.of(guardianDto, guardianDto2))
                .post(GUARDIAN_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(".", hasSize(2));
    }

    @Test
    void saveGuardianTest() {
        var guardianDto = TestConstants.stubGuardianDto();

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(guardianDto)
                .post(GUARDIAN_PATH + "/single")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("surname", is("Селезнева"));
    }
}
