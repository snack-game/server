package com.snackgame.server.game.snackgame.core.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.snackgame.server.game.snackgame.core.domain.item.policy.GrantType

data class GrantTypeRequest @JsonCreator constructor(
    @JsonProperty("provideType")
    val grantType: GrantType
)
