@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.snackgame.biz.controller

import com.snackgame.server.fixture.SeasonFixture
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠_인증정보
import com.snackgame.server.support.restassured.RestAssuredTest
import com.snackgame.server.support.restassured.RestAssuredUtil
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test

@RestAssuredTest
class SnackgameBizControllerTest {

    @Test
    fun `게임 결과가 서명된다`() {
        MemberFixture.saveAll()
        SeasonFixture.saveAll()
        RestAssuredUtil.givenAuthentication(땡칠_인증정보())
            .`when`().post("/games/4")

        RestAssuredUtil.givenAuthentication(땡칠_인증정보())
            .`when`().post("/games/4/1/end")
            .then().body(containsString(""""signed":"eyJhbGciOiJSUzI1NiJ9"""))
    }
}
