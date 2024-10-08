package com.kurdev.child_support_registry.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.kurdev.child_support_registry.mapper.ChildMapper;
import com.kurdev.child_support_registry.repository.DebtorsRepository;
import com.kurdev.child_support_registry.service.ChildrenService;
import com.kurdev.child_support_registry.service.DebtorService;
import com.kurdev.child_support_registry.stub.TestConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
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
public class DebtorControllerTest {

    public static final String DEBTOR_PATH = "/debtor";
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
    private DebtorsRepository debtorsRepository;
    @Autowired
    private ChildMapper childMapper;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        debtorsRepository.deleteAll();
    }

    @Test
    void getEmptyPage() {
        var result = given()
                .when()
                .get(DEBTOR_PATH);

        assertEquals(HttpStatus.SC_OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().as(JsonNode.class).get("totalElements").asInt());
    }

    @Test
    void getPageTest() {
        var debtor = TestConstants.stubDebtorDto();
        debtorService.create(List.of(debtor));

        var result = given()
                .when()
                .get(DEBTOR_PATH);

        assertEquals(HttpStatus.SC_OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().as(JsonNode.class).get("totalElements").asInt());
    }

    @Test
    void getDebtorByChildTest() {
        var debtor = TestConstants.stubDebtorDto();
        debtor = debtorService.create(List.of(debtor)).get(0);
        var child = TestConstants.stubChildDto();
        child.setDebtor(debtor);
        var children = childrenService.create(List.of(child));

        given()
                .when()
                .get(DEBTOR_PATH + "/by-child/" + children.get(0).getId())
                .then()
                .statusCode(200)
                .body("surname", is("Иванов"))
                .and()
                .body("name", is("Иван"));
    }

    @Test
    void saveDebtorsTest() {
        var debtorDto = TestConstants.stubDebtorDto();

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(List.of(debtorDto))
                .post(DEBTOR_PATH)
                .then()
                .statusCode(200)
                .body(".", hasSize(1));
    }

    @Test
    void saveDebtorTest() {
        var debtorDto = TestConstants.stubDebtorDto();

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(debtorDto)
                .post(DEBTOR_PATH + "/single")
                .then()
                .statusCode(200)
                .body("surname", is("Иванов"));
    }
}
