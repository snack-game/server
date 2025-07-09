package com.snackgame.server.game.snackgame.core.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.snackgame.server.game.snackgame.core.domain.item.ItemType
import com.snackgame.server.game.snackgame.core.domain.item.policy.GrantType


data class ItemGrantRequest @JsonCreator constructor(
    @JsonProperty("itemType")
    val itemType: ItemType,
    @JsonProperty("grantType")
    val grantType: GrantType
)
