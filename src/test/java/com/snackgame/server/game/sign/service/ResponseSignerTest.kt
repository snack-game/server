@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.sign.service

import com.snackgame.server.support.restassured.RestAssuredTest
import io.restassured.RestAssured
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestAssuredTest
class ResponseSignerTest {

    @TestConfiguration
    class TestConfig {

        @RestController
        class TestController {

            @Signed
            @GetMapping("/test")
            fun test(): Any {
                data class TestObject(val key: String)
                return TestObject("value")
            }
        }
    }

    @Test
    fun `Signed 어노테이션이 붙은 핸들러의 응답을 서명한다`() {
        val responseBody = RestAssured.given()
            .`when`().get("/test")
            .then().extract().body().asString()

        assertThat(responseBody).endsWith(
            """
            "signed":"eyJraWQiOiI3ODMwMjU4MTEiLCJhbGciOiJSUzI1NiJ9.eyJrZXkiOiJ2YWx1ZSJ9.
            h6BYhu1fwDdMso6_J0eZ-5P0VRBBFHg1ssGOuwU5yvEBWL4Ya1dRFPnl0vJAXcQEdVH8EAyxex1n
            zqby_PoZuiQ6azOKqyXfrMkG_kGtfwhNvmXMW92crSorp9UnHg9dHEEYWFabpeobI5BYXAN5_Cgl
            wSqwRlvJTwPqFNbyxrvO34ADBkHGh5fa251sWxYYfhTX5xxxatsvqno0RCItmQQ5Q3E9apzJInPU
            T4oRjN3GF2IGHvxyeVlNQW4I2tKNE5a6j2ECa3mgLNkaz0fYLVpoBaDqnw4U2pdG40jdGGWRlVui
            GYwtbr44-iJyRyuQDlhnhiK6403pS_BQEzLz2A"}
            """.replace("\\s+".toRegex(), "")
        )
    }
}
