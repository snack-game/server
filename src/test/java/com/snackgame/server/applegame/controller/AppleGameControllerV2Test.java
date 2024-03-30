package com.snackgame.server.applegame.controller;

import static com.snackgame.server.member.fixture.MemberFixture.땡칠;
import static com.snackgame.server.member.fixture.MemberFixture.땡칠_인증정보;
import static com.snackgame.server.member.fixture.MemberFixture.똥수;
import static com.snackgame.server.member.fixture.MemberFixture.똥수_인증정보;
import static com.snackgame.server.member.fixture.MemberFixture.정환;
import static com.snackgame.server.support.restassured.RestAssuredUtil.givenAuthentication;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.applegame.controller.dto.AppleGameResponseV2;
import com.snackgame.server.applegame.controller.dto.CoordinateRequest;
import com.snackgame.server.applegame.controller.dto.GameResultResponse;
import com.snackgame.server.applegame.controller.dto.RangeRequest;
import com.snackgame.server.applegame.domain.Coordinate;
import com.snackgame.server.applegame.domain.Range;
import com.snackgame.server.applegame.domain.game.AppleGame;
import com.snackgame.server.applegame.domain.game.AppleGames;
import com.snackgame.server.applegame.fixture.TestFixture;
import com.snackgame.server.fixture.SeasonFixture;
import com.snackgame.server.member.controller.dto.MemberDetailsResponse;
import com.snackgame.server.member.fixture.MemberFixture;
import com.snackgame.server.support.restassured.RestAssuredTest;

import io.restassured.http.ContentType;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@RestAssuredTest
class AppleGameControllerV2Test {

    @Autowired
    AppleGames appleGames;

    @BeforeEach
    void setUp() {
        MemberFixture.saveAll();
        SeasonFixture.saveAll();
    }

    @Test
    void 게임을_시작한다() {
        var response = givenAuthentication(땡칠_인증정보())
                .when().post("/v2/games/1")
                .then().statusCode(OK.value())
                .extract().as(AppleGameResponseV2.class);

        assertThat(response.getSessionId()).isNotNull();
        assertThat(response.getApples()).hasSize(AppleGame.DEFAULT_HEIGHT);
        assertThat(response.getApples().get(0)).hasSize(AppleGame.DEFAULT_WIDTH);
    }

    @Test
    void 게임을_조작한다() {
        var game = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId()));
        List<RangeRequest> rangeRequests = List.of(
                new RangeRequest(
                        new CoordinateRequest(0, 1),
                        new CoordinateRequest(1, 3)
                ),
                new RangeRequest(
                        new CoordinateRequest(0, 0),
                        new CoordinateRequest(1, 0)
                )
        );

        givenAuthentication(땡칠_인증정보())
                .contentType(ContentType.JSON)
                .body(rangeRequests)
                .when().put("/v2/sessions/{sessionId}/moves", game.getSessionId())
                .then().statusCode(OK.value());
    }

    @Test
    void 황금사과를_제거하면_초기화된_판을_반환한다() {
        var game = appleGames.save(new AppleGame(TestFixture.TWO_BY_TWO_WITH_GOLDEN_APPLE(), 땡칠().getId()));
        List<RangeRequest> rangeRequests = List.of(new RangeRequest(
                new CoordinateRequest(0, 0),
                new CoordinateRequest(1, 0))
        );

        givenAuthentication(땡칠_인증정보())
                .contentType(ContentType.JSON)
                .body(rangeRequests)
                .when().put("/v2/sessions/{sessionId}/moves", game.getSessionId())
                .then().statusCode(CREATED.value())
                .extract().as(AppleGameResponseV2.class);
    }

    @Test
    void 황금사과를_제거하지_않으면_판을_반환하지_않는다() {
        var game = appleGames.save(new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId()));
        var rangeRequests = List.of(new RangeRequest(
                new CoordinateRequest(0, 0),
                new CoordinateRequest(1, 0))
        );

        givenAuthentication(땡칠_인증정보())
                .contentType(ContentType.JSON)
                .body(rangeRequests)
                .when().put("/v2/sessions/{sessionId}/moves", game.getSessionId())
                .then().statusCode(OK.value())
                .body(emptyOrNullString());
    }

    @Test
    void 게임을_재시작한다() {
        var started = givenAuthentication(땡칠_인증정보())
                .when().post("/v2/games/1")
                .then().statusCode(OK.value())
                .extract().as(AppleGameResponseV2.class);
        givenAuthentication(땡칠_인증정보())
                .contentType(ContentType.JSON)
                .body(List.of(new RangeRequest(
                        new CoordinateRequest(0, 0),
                        new CoordinateRequest(1, 0))
                ))
                .when().put("/v2/sessions/{sessionId}/moves", started.getSessionId());

        var restarted = givenAuthentication(땡칠_인증정보())
                .when().delete("/v2/sessions/{sessionId}/board", started.getSessionId())
                .then().statusCode(OK.value())
                .extract().as(AppleGameResponseV2.class);

        assertThat(started.getSessionId()).isEqualTo(restarted.getSessionId());
        assertThat(restarted.getScore()).isZero();
        assertThat(started.getApples())
                .usingRecursiveComparison()
                .isNotEqualTo(restarted.getApples());
    }

    @Test
    void 게임을_끝낸다() {
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 똥수().getId());
        game.removeApplesIn(new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3))
        );
        appleGames.save(game);

        givenAuthentication(똥수_인증정보())
                .when().put("/v2/sessions/{sessionId}/end", game.getSessionId())
                .then().log().all()
                .statusCode(OK.value())
                .extract().as(GameResultResponse.class);
    }

    @Test
    void 게임을_끝낼_때_점수와_백분율을_알_수_있다() {
        {
            var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 정환().getId());
            game.removeApplesIn(new Range(
                    new Coordinate(0, 1),
                    new Coordinate(1, 3))
            );
            game.finish();
            appleGames.save(game);
        }
        {
            var otherGame = new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId());
            otherGame.finish();
            appleGames.save(otherGame);
        }

        var gameWithMidRangedScore = new AppleGame(TestFixture.TWO_BY_FOUR(), 땡칠().getId());
        gameWithMidRangedScore.removeApplesIn(new Range(
                new Coordinate(0, 0),
                new Coordinate(1, 0))
        );
        appleGames.save(gameWithMidRangedScore);

        var result = givenAuthentication(땡칠_인증정보())
                .when().put("/v2/sessions/{sessionId}/end", gameWithMidRangedScore.getSessionId())
                .then()
                .statusCode(OK.value())
                .extract().as(GameResultResponse.class);

        assertThat(result.getScore()).isEqualTo(2);
        assertThat(result.getPercentile()).isEqualTo(50);
    }

    @Test
    void 게임이_끝날때_게임_점수만큼_경험치를_부여한다() {
        var game = new AppleGame(TestFixture.TWO_BY_FOUR(), 똥수().getId());
        game.removeApplesIn(new Range(
                new Coordinate(0, 1),
                new Coordinate(1, 3))
        );
        appleGames.save(game);

        givenAuthentication(똥수_인증정보())
                .when().put("/v2/sessions/{sessionId}/end", game.getSessionId())
                .then().statusCode(OK.value());

        var member = givenAuthentication(똥수_인증정보())
                .when().get("/members/me")
                .then()
                .statusCode(OK.value())
                .extract().as(MemberDetailsResponse.class);

        assertThat(member.getStatus().getExp()).isEqualTo(4);
    }
}
