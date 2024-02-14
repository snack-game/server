package com.snackgame.server.member.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.snackgame.server.support.restassured.RestAssuredTest;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@RestAssuredTest
class AuthControllerTest {

    @Test
    void 토큰을_발급한다() {
        var cookies = RestAssured.given().log().all()
                .when().post("/tokens/guest")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("accessToken", startsWith("eyJhbGciOiJIUzI1NiJ9"))
                .cookie("accessToken", startsWith("eyJhbGciOiJIUzI1NiJ9"))
                .cookie("refreshToken", startsWith("eyJhbGciOiJIUzI1NiJ9"))
                .extract().detailedCookies();

        assertThat(cookies.get("accessToken").getSameSite()).isEqualTo("None");
        assertThat(cookies.get("refreshToken").getSameSite()).isEqualTo("None");
    }

    @Test
    void 토큰을_쿠키에_싣는다() {
        RestAssured.given()
                .when().post("/tokens/guest")
                .then().log().all()
                .cookie("accessToken", startsWith("eyJhbGciOiJIUzI1NiJ9"))
                .cookie("refreshToken", startsWith("eyJhbGciOiJIUzI1NiJ9"));
    }

    @Test
    void 리프레시_토큰으로_토큰을_재발급한다() throws InterruptedException {
        var cookies = RestAssured.given()
                .when().post("/tokens/guest")
                .then().extract().detailedCookies();

        Thread.sleep(1000);

        RestAssured.given().log().all()
                .cookies(cookies)
                .when().patch("/tokens/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("accessToken", startsWith("eyJhbGciOiJIUzI1NiJ9"))
                .cookie("accessToken", startsWith("eyJhbGciOiJIUzI1NiJ9"))
                .cookie("refreshToken", startsWith("eyJhbGciOiJIUzI1NiJ9"))
                .cookie("refreshToken", not(cookies.getValue("refreshToken")));
    }

    @Test
    void 로그아웃할때_토큰을_삭제한다() {
        var cookies = RestAssured.given()
                .when().post("/tokens/guest")
                .then().extract().detailedCookies();

        RestAssured.given().log().all()
                .cookies(cookies)
                .when().delete("/tokens/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .cookie("accessToken", is(emptyString()))
                .cookie("refreshToken", is(emptyString()));
    }
}
