@file:Suppress("NonAsciiCharacters")

package com.snackgame.server.game.snackgame.fixture


import com.snackgame.server.game.snackgame.core.domain.item.Item
import com.snackgame.server.game.snackgame.core.domain.item.ItemType
import com.snackgame.server.member.fixture.MemberFixture
import com.snackgame.server.member.fixture.MemberFixture.땡칠
import com.snackgame.server.support.fixture.FixtureSaver
import java.time.LocalDateTime

object ItemFixture {

    @JvmStatic
    fun 땡칠_폭탄(): Item {
        return Item(땡칠().id, ItemType.BOMB, 1, LocalDateTime.now(), 1)
    }

    @JvmStatic
    fun 땡칠_피버(): Item {
        return Item(땡칠().id, ItemType.FEVER_TIME, 1, LocalDateTime.now(), 2)
    }

    @JvmStatic
    fun saveAll() {
        MemberFixture.saveAll()
        FixtureSaver.save(
            땡칠_폭탄(),
            땡칠_피버()
        )
    }
}