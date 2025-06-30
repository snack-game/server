package com.snackgame.server.game.snackgame.core.controller

import com.snackgame.server.game.snackgame.core.domain.item.ItemService
import com.snackgame.server.game.snackgame.core.domain.item.ItemType
import com.snackgame.server.game.snackgame.core.service.dto.ItemCountResponse
import com.snackgame.server.game.snackgame.fixture.ItemFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠_인증정보
import com.snackgame.server.support.restassured.RestAssuredTest
import com.snackgame.server.support.restassured.RestAssuredUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

@RestAssuredTest
class SnackgameControllerTest {

    lateinit var itemService: ItemService

    @BeforeEach
    fun setUp() {
        ItemFixture.saveAll()
    }

    @Test
    fun `사용자가 보유한 아이템 개수를 조회한다`() {

        val response = RestAssuredUtil.givenAuthentication(땡칠_인증정보())
            .given()
            .`when`()
            .get("/games/2/items")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract().`as`(ItemCountResponse::class.java)

        assertThat(response.items[ItemType.BOMB]).isEqualTo(1)
        assertThat(response.items[ItemType.FEVER_TIME]).isEqualTo(1)
    }

}