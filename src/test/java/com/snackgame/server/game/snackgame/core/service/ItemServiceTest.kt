@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.snackgame.core.service


import com.snackgame.server.game.snackgame.core.domain.item.ItemGrantHistories
import com.snackgame.server.game.snackgame.core.domain.item.ItemGrantHistory
import com.snackgame.server.game.snackgame.core.domain.item.ItemRepository
import com.snackgame.server.game.snackgame.core.domain.item.ItemService
import com.snackgame.server.game.snackgame.core.domain.item.ItemType
import com.snackgame.server.game.snackgame.core.domain.item.policy.GrantType
import com.snackgame.server.game.snackgame.exception.ItemNotReadyException
import com.snackgame.server.game.snackgame.fixture.ItemFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.member.fixture.MemberFixture.정환
import com.snackgame.server.support.general.ServiceTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

@ServiceTest
class ItemServiceTest {

    @Autowired
    private lateinit var itemService: ItemService

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var itemGrantHistories: ItemGrantHistories

    @BeforeEach
    fun setUp() {
        ItemFixture.saveAll()
    }

    @Test

    fun `특정 아이템을 하나 획득할 수 있다`() {
        itemService.issueItem(땡칠().id, ItemType.BOMB, GrantType.DAILY)

        val found = itemRepository.findItemByOwnerIdAndItemType(땡칠().id, ItemType.BOMB)

        assertThat(found.get().count).isEqualTo(2)
    }

    @Test
    fun `아이템을 보유하지 않고 있다면 새로 만들어서 제공한다`() {
        val notIssuedYet = itemRepository.findItemByOwnerIdAndItemType(정환().id, ItemType.BOMB)
        assertThat(notIssuedYet.isEmpty).isTrue()

        itemService.issueItem(정환().id, ItemType.BOMB, GrantType.DAILY)

        val issued = itemRepository.findItemByOwnerIdAndItemType(정환().id, ItemType.BOMB)
        assertThat(issued.get().count).isEqualTo(1)
    }

    @Test
    fun `아이템 조회 시 발급받은 적이 없다면 모든 아이템을 새로 만들어서 제공한다`() {
        val notIssuedYet = itemRepository.findAllByOwnerId(정환().id)
        assertThat(notIssuedYet.isEmpty()).isTrue()

        val checked = itemService.checkItemPresence(정환().id)
        assertThat(checked.items.size).isEqualTo(ItemType.entries.size)
    }

    @Test
    fun `아이템은 하루에 한번만 발급받을 수 있다`() {
        itemGrantHistories.save(
            ItemGrantHistory(
                땡칠().id, ItemType.BOMB,
                GrantType.DAILY, LocalDateTime.now()
            )
        )

        assertThatThrownBy { itemService.issueItem(땡칠().id, ItemType.BOMB, GrantType.DAILY) }
            .isInstanceOf(ItemNotReadyException::class.java)
    }

}