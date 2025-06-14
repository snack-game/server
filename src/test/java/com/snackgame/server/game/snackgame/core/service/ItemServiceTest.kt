@file:Suppress("NonAsciiCharacters")
package com.snackgame.server.game.snackgame.core.service


import com.snackgame.server.game.snackgame.core.domain.item.Item
import com.snackgame.server.game.snackgame.core.domain.item.ItemRepository
import com.snackgame.server.game.snackgame.core.domain.item.ItemService
import com.snackgame.server.game.snackgame.core.domain.item.ItemType
import com.snackgame.server.game.snackgame.core.service.dto.ItemTypeRequest
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@ServiceTest
class ItemServiceTest {

    @Autowired
    private lateinit var itemService: ItemService
    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Test
    fun `아이템의 보유 여부를 알 수 있다`(){

        val bomb: Item = Item(땡칠().id, ItemType.BOMB,1, LocalDateTime.now(), 1)
        itemRepository.save(bomb)

        val presence = itemService.checkItemPresence(땡칠().id, ItemTypeRequest(ItemType.BOMB))
        assertThat(presence).isTrue()
    }

}