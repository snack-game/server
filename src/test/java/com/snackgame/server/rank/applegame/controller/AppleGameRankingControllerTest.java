package com.snackgame.server.rank.applegame.controller;

import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_땡칠_10점;
import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_똥수_10점;
import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_유진_6점;
import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_정언_8점;
import static com.snackgame.server.fixture.BestScoreFixture.베타시즌_정환_8점;
import static com.snackgame.server.fixture.BestScoreFixture.시즌1_땡칠_20점;
import static com.snackgame.server.fixture.BestScoreFixture.시즌1_유진_20점;
import static com.snackgame.server.fixture.BestScoreFixture.시즌1_정언_8점;
import static com.snackgame.server.fixture.BestScoreFixture.시즌1_정환_20점;
import static com.snackgame.server.fixture.SeasonFixture.시즌1;
import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.snackgame.server.fixture.BestScoreFixture;
import com.snackgame.server.member.controller.dto.NameRequest;
import com.snackgame.server.support.restassured.RestAssuredTest;

import io.restassured.RestAssured;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@RestAssuredTest
class AppleGameRankingControllerTest {

    @BeforeEach
    void setUp() {
        BestScoreFixture.saveAll();
    }

    @Nested
    class 전체_시즌_최고점수_기준 {

        @Test
        void 선두_50등을_조회한다() {
            RestAssured.given()
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0].owner.id", is(시즌1_땡칠_20점().getOwnerId().intValue()))
                    .body("[0].score", is(20))
                    .body("[1].owner.id", is(시즌1_정환_20점().getOwnerId().intValue()))
                    .body("[1].score", is(20))
                    .body("[2].owner.id", is(시즌1_유진_20점().getOwnerId().intValue()))
                    .body("[2].score", is(20))
                    .body("[3].owner.id", is(베타시즌_똥수_10점().getOwnerId().intValue()))
                    .body("[3].score", is(10))
                    .body("[4].owner.id", is(베타시즌_땡칠_10점().getOwnerId().intValue()))
                    .body("[4].score", is(10))
                    .body("[5].owner.id", is(베타시즌_정환_8점().getOwnerId().intValue()))
                    .body("[5].score", is(8))
                    .body("[6].owner.id", is(베타시즌_정언_8점().getOwnerId().intValue()))
                    .body("[6].score", is(8))
                    .body("[7].owner.id", is(시즌1_정언_8점().getOwnerId().intValue()))
                    .body("[7].score", is(8))
                    .body("[8].owner.id", is(베타시즌_유진_6점().getOwnerId().intValue()))
                    .body("[8].score", is(6));
        }

        @Test
        void 공동_1등_세명_다음은_4등이다() {
            RestAssured.given()
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0].rank", is(1))
                    .body("[1].rank", is(1))
                    .body("[2].rank", is(1))
                    .body("[3].rank", is(4));
        }

        @Test
        void 자신의_랭크를_조회한다() {
            var authentication = RestAssured.given()
                    .contentType(JSON)
                    .body(new NameRequest(땡칠().getNameAsString()))
                    .when().post("/tokens")
                    .then().extract().detailedCookies();

            RestAssured.given()
                    .cookies(authentication)
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings/me")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("rank", is(1))
                    .body("owner.id", is(시즌1_땡칠_20점().getOwnerId().intValue()));
        }
    }

    @Nested
    class 특정_시즌_최고점수_기준 {

        @Test
        void 선두_50등을_조회한다() {
            RestAssured.given()
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings/{seasonId}", 시즌1().getId())
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0].owner.id", is(시즌1_땡칠_20점().getOwnerId().intValue()))
                    .body("[1].owner.id", is(시즌1_정환_20점().getOwnerId().intValue()))
                    .body("[2].owner.id", is(시즌1_유진_20점().getOwnerId().intValue()))
                    .body("[3].owner.id", is(시즌1_정언_8점().getOwnerId().intValue()));
        }

        @Test
        void 공동_1등_세명_다음은_4등이다() {
            RestAssured.given()
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings/{seasonId}", 시즌1().getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0].rank", is(1))
                    .body("[1].rank", is(1))
                    .body("[2].rank", is(1))
                    .body("[3].rank", is(4));
        }

        @Test
        void 자신의_랭크를_조회한다() {
            var authentication = RestAssured.given()
                    .contentType(JSON)
                    .body(new NameRequest(땡칠().getNameAsString()))
                    .when().post("/tokens")
                    .then().extract().detailedCookies();

            RestAssured.given()
                    .cookies(authentication)
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings/{seasonId}/me", 시즌1().getId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("rank", is(1))
                    .body("owner.id", is(시즌1_땡칠_20점().getOwnerId().intValue()));
        }
    }
}
