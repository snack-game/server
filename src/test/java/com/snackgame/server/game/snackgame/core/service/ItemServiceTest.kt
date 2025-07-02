@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.snackgame.core.service



import com.snackgame.server.fixture.SeasonFixture
import com.snackgame.server.game.snackgame.core.domain.item.Item
import com.snackgame.server.game.snackgame.core.domain.item.ItemRepository
import com.snackgame.server.game.snackgame.core.domain.item.ItemService
import com.snackgame.server.game.snackgame.core.domain.item.ItemType
import com.snackgame.server.game.snackgame.fixture.BoardFixture
import com.snackgame.server.game.snackgame.fixture.ItemFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.member.fixture.MemberFixture.정환
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired

@ServiceTest
class ItemServiceTest {

    @Autowired
    private lateinit var itemService: ItemService

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @BeforeEach
    fun setUp() {
        ItemFixture.saveAll()
    }

    @Test
    fun `특정 아이템을 하나 획득할 수 있다`(){
        itemService.issueItem(땡칠().id, ItemType.BOMB)

        val found = itemRepository.findItemByOwnerIdAndItemType(땡칠().id, ItemType.BOMB)

        assertThat(found.get().count).isEqualTo(2)
    }

    @Test
    fun `아이템을 보유하지 않고 있다면 새로 만들어서 제공한다`(){
        val notIssuedYet = itemRepository.findItemByOwnerIdAndItemType(정환().id, ItemType.BOMB)
        assertThat(notIssuedYet.isEmpty).isTrue()

        itemService.issueItem(정환().id, ItemType.BOMB)

        val issued = itemRepository.findItemByOwnerIdAndItemType(정환().id, ItemType.BOMB)
        assertThat(issued.get().count).isEqualTo(1)
    }


}