package com.library.librarysystem.controller;

import com.library.librarysystem.model.User;
import com.library.librarysystem.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        userRepository.deleteAll(); // Clean database before each test
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll(); // Clean database after each test
    }

    @Test
    void testRegisterUserSuccessfully() {
        String requestBody = "{ \"email\": \"testuser@example.com\", \"password\": \"password123\" }";

        given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(200) // Validate response code
                .body(equalTo("User registered successfully!")); // Validate payload
    }

    @Test
    void testLoginUserSuccessfully() {
        // First, register a user to login with
        String email = "loginuser@example.com";
        String password = "password123";
        String registerBody = String.format("{ \"email\": \"%s\", \"password\": \"%s\" }", email, password);

        given().contentType(ContentType.JSON).body(registerBody).when().post("/api/auth/register");

        // Now, test the login
        String loginBody = String.format("{ \"email\": \"%s\", \"password\": \"%s\" }", email, password);

        given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200) // Validate response code
                .body("token", notNullValue()); // Validate payload contains a token
    }
}