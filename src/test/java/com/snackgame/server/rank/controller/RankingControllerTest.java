package com.snackgame.server.rank.controller;

import static com.snackgame.server.fixture.BestScoreFixture.사과게임_베타시즌_땡칠_10점;
import static com.snackgame.server.fixture.BestScoreFixture.사과게임_베타시즌_똥수_10점;
import static com.snackgame.server.fixture.BestScoreFixture.사과게임_베타시즌_유진_6점;
import static com.snackgame.server.fixture.BestScoreFixture.사과게임_베타시즌_정언_8점;
import static com.snackgame.server.fixture.BestScoreFixture.사과게임_베타시즌_정환_8점;
import static com.snackgame.server.fixture.BestScoreFixture.사과게임_시즌1_땡칠_20점;
import static com.snackgame.server.fixture.BestScoreFixture.사과게임_시즌1_유진_20점;
import static com.snackgame.server.fixture.BestScoreFixture.사과게임_시즌1_정언_8점;
import static com.snackgame.server.fixture.BestScoreFixture.사과게임_시즌1_정환_20점;
import static com.snackgame.server.fixture.BestScoreFixture.스낵게임_시즌1_땡칠_20점;
import static com.snackgame.server.fixture.BestScoreFixture.스낵게임_시즌1_유진_20점;
import static com.snackgame.server.fixture.BestScoreFixture.스낵게임_시즌1_정언_8점;
import static com.snackgame.server.fixture.SeasonFixture.베타시즌;
import static com.snackgame.server.fixture.SeasonFixture.시즌1;
import static com.snackgame.server.game.metadata.Metadata.APPLE_GAME;
import static com.snackgame.server.game.metadata.Metadata.SNACK_GAME;
import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.support.restassured.RestAssuredUtil.givenAuthentication;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import com.snackgame.server.fixture.BestScoreFixture;
import com.snackgame.server.game.metadata.Metadata;
import com.snackgame.server.member.controller.dto.NameRequest;
import com.snackgame.server.rank.controller.dto.RankOwnerResponse;
import com.snackgame.server.rank.controller.dto.RankResponseV2;
import com.snackgame.server.rank.domain.Season;
import com.snackgame.server.support.restassured.RestAssuredTest;
import com.snackgame.server.support.restassured.RestAssuredUtil;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@RestAssuredTest
class RankingControllerTest {

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
                    .when().get("/rankings/{gameId}", APPLE_GAME.getGameId())
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0].owner.id", is((int)사과게임_시즌1_땡칠_20점().getOwnerId()))
                    .body("[0].score", is(20))
                    .body("[1].owner.id", is((int)사과게임_시즌1_정환_20점().getOwnerId()))
                    .body("[1].score", is(20))
                    .body("[2].owner.id", is((int)사과게임_시즌1_유진_20점().getOwnerId()))
                    .body("[2].score", is(20))
                    .body("[3].owner.id", is((int)사과게임_베타시즌_똥수_10점().getOwnerId()))
                    .body("[3].score", is(10))
                    .body("[4].owner.id", is((int)사과게임_베타시즌_땡칠_10점().getOwnerId()))
                    .body("[4].score", is(10))
                    .body("[5].owner.id", is((int)사과게임_베타시즌_정환_8점().getOwnerId()))
                    .body("[5].score", is(8))
                    .body("[6].owner.id", is((int)사과게임_베타시즌_정언_8점().getOwnerId()))
                    .body("[6].score", is(8))
                    .body("[7].owner.id", is((int)사과게임_시즌1_정언_8점().getOwnerId()))
                    .body("[7].score", is(8))
                    .body("[8].owner.id", is((int)사과게임_베타시즌_유진_6점().getOwnerId()))
                    .body("[8].score", is(6));
        }

        @Test
        void 랭크는_멤버_속성도_포함한다() {
            List<RankResponseV2> rankResponses = RestAssured.given()
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings/{gameId}", APPLE_GAME.getGameId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract().as(new TypeRef<>() {
                    });

            assertThat(rankResponses).first()
                    .usingRecursiveComparison()
                    .ignoringFields("owner.status.exp", "owner.status.maxExp")
                    .isEqualTo(new RankResponseV2(
                            1,
                            사과게임_시즌1_땡칠_20점().getGameId(),
                            시즌1().getId(),
                            사과게임_시즌1_땡칠_20점().getScore(),
                            RankOwnerResponse.of(땡칠())
                    ));
        }

        @Test
        void 공동_1등_세명_다음은_4등이다() {
            RestAssured.given()
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings/{gameId}", APPLE_GAME.getGameId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0].rank", is(1))
                    .body("[1].rank", is(1))
                    .body("[2].rank", is(1))
                    .body("[3].rank", is(4));
        }

        @Test
        void 자신의_랭크를_조회한다() {
            RestAssuredUtil.givenAuthentication(new NameRequest(땡칠().getNameAsString()))
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings/{gameId}/me", APPLE_GAME.getGameId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("rank", is(1))
                    .body("owner.id", is((int)사과게임_시즌1_땡칠_20점().getOwnerId()));
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class 특정_시즌_최고점수_기준 {

        @Test
        void 사과게임의_선두_50등을_조회한다() {
            RestAssured.given()
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings/{seasonId}/{gameId}", 시즌1().getId(), APPLE_GAME.getGameId())
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0].owner.id", is((int)사과게임_시즌1_땡칠_20점().getOwnerId()))
                    .body("[1].owner.id", is((int)사과게임_시즌1_정환_20점().getOwnerId()))
                    .body("[2].owner.id", is((int)사과게임_시즌1_유진_20점().getOwnerId()))
                    .body("[3].owner.id", is((int)사과게임_시즌1_정언_8점().getOwnerId()));
        }

        @Test
        void 스낵게임의_선두_50등을_조회한다() {
            RestAssured.given()
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings/{seasonId}/{gameId}", 시즌1().getId(), SNACK_GAME.getGameId())
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0].owner.id", is((int)스낵게임_시즌1_땡칠_20점().getOwnerId()))
                    .body("[1].owner.id", is((int)스낵게임_시즌1_유진_20점().getOwnerId()))
                    .body("[2].owner.id", is((int)스낵게임_시즌1_정언_8점().getOwnerId()));
        }

        private Stream<Arguments> 자신의_랭크를_조회한다() {
            return Stream.of(
                    Arguments.of(1, 10, 베타시즌(), APPLE_GAME),
                    Arguments.of(1, 20, 시즌1(), APPLE_GAME),
                    Arguments.of(1, 20, 시즌1(), SNACK_GAME)
            );
        }

        @ParameterizedTest(name = "{0}등, {1}점, {2}")
        @MethodSource
        void 자신의_랭크를_조회한다(long expectedRank, int expectedScore, Season season, Metadata expectedMetadata) {
            var response = givenAuthentication(new NameRequest(땡칠().getNameAsString()))
                    .queryParam("by", "BEST_SCORE")
                    .when().get("/rankings/{seasonId}/{gameId}/me", season.getId(), expectedMetadata.getGameId())
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract().as(RankResponseV2.class);

            assertThat(response)
                    .usingRecursiveComparison()
                    .ignoringFields("owner.status.exp", "owner.status.maxExp")
                    .isEqualTo(new RankResponseV2(
                            expectedRank,
                            expectedMetadata.getGameId(),
                            season.getId(),
                            expectedScore,
                            RankOwnerResponse.of(땡칠())
                    ));
        }
    }
}
