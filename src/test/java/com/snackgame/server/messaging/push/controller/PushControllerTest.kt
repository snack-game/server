@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.messaging.push.controller

import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠_인증정보
import com.snackgame.server.messaging.push.service.dto.DeviceTokenRequest
import com.snackgame.server.support.restassured.RestAssuredTest
import com.snackgame.server.support.restassured.RestAssuredUtil
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

@RestAssuredTest
class PushControllerTest {

    @BeforeEach
    fun setUp() {
        MemberFixture.saveAll()
    }

    @Test
    fun `기기를 등록한다`() {
        RestAssuredUtil.givenAuthentication(땡칠_인증정보())
            .contentType(ContentType.JSON)
            .body(DeviceTokenRequest("a_device_token"))
            .`when`().post("/notifications/devices")
            .then().statusCode(HttpStatus.OK.value())
    }
}
