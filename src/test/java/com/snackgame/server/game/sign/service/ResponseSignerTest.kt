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

        assertThat(responseBody).contains(
            """
            "signed":"eyJhbGciOiJSUzI1NiJ9.eyJrZXkiOiJ2YWx1ZSJ9.Jrg2SkMT2c3LIMR_pIKmdEF2-3UyH1r8nS-
            q43l5KmKN1hQve4EG18JD5btvGMfZEZu2vjwRmgP-ybhKBNt0-khZ8zBGh3YQ4FILkMETyS-7ObJD
            6N_737BjVa0iMg7MKb6_enoTDJ_p-6_murR7Y1ujSGrGJWSvzdO9FooQTetc7LQorQXUhjk5Wm-ggQ
            rMnTSTYlNPaSuXrtdVzB3jbMbC0jK-rcr0-sntFCvC2WCC_vRcr7aRZk72RVBI5EEDy5DW_kxIRrbT
            dUSglBx5YPkCec9XKZtGKpFUhgWkyfKjEUgYgfg6e6AEzJPMuX5L2V6LKwBdn4TSkh4u3pyk-g"
            """.replace("\\s+".toRegex(), "")
        )
    }
}
