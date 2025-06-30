@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.snackgame.core.service


import com.snackgame.server.game.snackgame.core.domain.item.ItemRepository
import com.snackgame.server.game.snackgame.core.domain.item.ItemService
import com.snackgame.server.support.general.ServiceTest
import org.springframework.beans.factory.annotation.Autowired

@ServiceTest
class ItemServiceTest {

    @Autowired
    private lateinit var itemService: ItemService

    @Autowired
    private lateinit var itemRepository: ItemRepository


}