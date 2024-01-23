package com.snackgame.server.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import com.snackgame.server.auth.token.domain.RefreshTokenRepository;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 토큰을_발급한다() {
        RestAssured.given().log().all()
                .when().post("/tokens/guest")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("accessToken", startsWith("eyJhbGciOiJIUzI1NiJ9"))
                .cookie("refreshToken", startsWith("eyJhbGciOiJIUzI1NiJ9"));
    }

    @Test
    void 리프레시_토큰으로_토큰을_재발급한다() throws InterruptedException {
        Cookie refreshToken = RestAssured.given()
                .when().post("/tokens/guest")
                .then().extract().detailedCookie("refreshToken");

        Thread.sleep(1000);

        RestAssured.given().log().all()
                .cookie(refreshToken)
                .when().post("/tokens/reissue")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("accessToken", startsWith("eyJhbGciOiJIUzI1NiJ9"))
                .cookie("refreshToken", startsWith("eyJhbGciOiJIUzI1NiJ9"))
                .cookie("refreshToken", not(refreshToken.getValue()));
    }

    @Test
    void 로그아웃할때_리프레시_토큰을_삭제한다() throws InterruptedException {
        Cookie refresh_cookie = RestAssured.given()
                .when().post("/tokens/guest")
                .then().extract().detailedCookie("refreshToken");

        assertThat(refreshTokenRepository.findAll()).hasSize(1);

        RestAssured.given().log().all()
                .cookie(refresh_cookie)
                .when().delete("/tokens")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        assertThat(refreshTokenRepository.findAll()).hasSize(0);

    }

}
