package com.kurdev.child_support_registry.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.kurdev.child_support_registry.repository.ChildrenRepository;
import com.kurdev.child_support_registry.service.ChildrenService;
import com.kurdev.child_support_registry.service.DebtorService;
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
public class ChildrenControllerTest {

    public static final String CHILDREN_PATH = "/children";
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
                .get(CHILDREN_PATH);

        assertEquals(HttpStatus.OK.value(), result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().as(JsonNode.class).get("totalElements").asInt());
    }

    @Test
    void getPageTest() {
        var child = TestConstants.stubChildDto();
        childrenService.create(List.of(child));

        var result = given()
                .when()
                .get(CHILDREN_PATH);

        assertEquals(HttpStatus.OK.value(), result.getStatusCode());
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
                .get(CHILDREN_PATH + "/by-guardian/" + TestConstants.TEST_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
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
                .get(CHILDREN_PATH + "/by-debtor/" + TestConstants.TEST_ID)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(".", hasSize(1));
    }

    @Test
    void saveChildrenTest() {
        var childDto = TestConstants.stubChildDto();
        var childDto2 = TestConstants.stubChildDto();
        childDto2.setName("Александр");

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(List.of(childDto, childDto2))
                .post(CHILDREN_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(".", hasSize(2));
    }

    @Test
    void saveChildTest() {
        var childDto = TestConstants.stubChildDto();

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(childDto)
                .post(CHILDREN_PATH + "/single")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("surname", is("Иванов"));
    }
}
