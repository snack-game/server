package com.snackgame.server.support.restassured;

import com.snackgame.server.member.controller.dto.NameRequest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestAssuredUtil {

    public static RequestSpecification givenAuthentication(NameRequest nameRequest) {
        var cookies = RestAssured.given()
                .body(nameRequest)
                .contentType(ContentType.JSON)
                .when().post("/tokens")
                .then().extract().detailedCookies();

        return RestAssured.given().cookies(cookies);
    }
}
