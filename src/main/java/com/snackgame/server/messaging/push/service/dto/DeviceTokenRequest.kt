package com.snackgame.server.messaging.push.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls

data class DeviceTokenRequest @JsonCreator constructor(
    @JsonSetter(nulls = Nulls.FAIL)
    val token: String
)
