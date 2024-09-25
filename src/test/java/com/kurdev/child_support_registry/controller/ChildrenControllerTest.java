package com.kurdev.child_support_registry.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.kurdev.child_support_registry.repository.ChildrenRepository;
import com.kurdev.child_support_registry.service.ChildrenService;
import com.kurdev.child_support_registry.service.DebtorService;
import com.kurdev.child_support_registry.service.GuardianService;
import com.kurdev.child_support_registry.stub.TestConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChildrenControllerTest {

    @LocalServerPort
    private Integer port;

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
    private DebtorService debtorService;
    @Autowired
    private GuardianService guardianService;
    @Autowired
    private ChildrenRepository childrenRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        childrenRepository.deleteAll();
    }

    @Test
    void getEmptyPage() {
        var result = given()
                .when()
                .get("/children");

        assertEquals(HttpStatus.SC_OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().as(JsonNode.class).get("totalElements").asInt());
    }

    @Test
    void getPageTest() {
        var child = TestConstants.stubChildDto();
        childrenService.create(List.of(child));

        var result = given()
                .when()
                .get("/children");

        assertEquals(HttpStatus.SC_OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().as(JsonNode.class).get("totalElements").asInt());
    }

    @Test
    void getChildrenByGuardianTest() {
        var guardian = TestConstants.stubGuardianDto();
        guardianService.create(List.of(guardian));
        var child = TestConstants.stubChildDto();
        child.setGuardian(guardian);
        childrenService.create(List.of(child));

        given()
                .when()
                .get("/children/by-guardian/" + TestConstants.TEST_ID)
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    void getChildrenByDebtorTest() {
        var debtor = TestConstants.stubDebtorDto();
        debtorService.create(List.of(debtor));
        var child = TestConstants.stubChildDto();
        child.setDebtor(debtor);
        childrenService.create(List.of(child));

        given()
                .when()
                .get("/children/by-debtor/" + TestConstants.TEST_ID)
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    void saveChildrenTest() {
        var childDto = TestConstants.stubChildDto();

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(List.of(childDto))
                .post("/children")
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    void saveChildTest() {
        var childDto = TestConstants.stubChildDto();

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(childDto)
                .post("/children/single")
                .then()
                .statusCode(200)
                .body("surname", is("Иванов"))
                .and()
                .body("id", Matchers.notNullValue());


    }
}
