package com.snackgame.server.auth.token.support;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.support.restassured.RestAssuredTest;

import io.restassured.RestAssured;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@RestAssuredTest
class JwtMemberArgumentResolverTest {

    @RestController
    static class FakeController {

        public static final String AUTHORIZED_ENDPOINT = "/authorized/endpoint";
        public static final String EXPECTED_RESPONSE = "test-pass";

        @GetMapping(AUTHORIZED_ENDPOINT)
        public String authenticatedEndpoint(@Authenticated Object object) {
            return EXPECTED_RESPONSE;
        }
    }

    @Nested
    class 액세스_토큰을 {

        @Test
        void 쿠키에서_읽는다() {
            String accessToken = getValidAccessToken();

            RestAssured.given()
                    .cookie("accessToken", accessToken)
                    .when().get(FakeController.AUTHORIZED_ENDPOINT)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body(equalTo(FakeController.EXPECTED_RESPONSE));
        }

        private String getValidAccessToken() {
            return RestAssured.given()
                    .when().post("/tokens/guest")
                    .then().extract().detailedCookie("accessToken")
                    .getValue();
        }
    }
}
