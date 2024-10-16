package com.snackgame.server.messaging.notification.service.dto

import com.snackgame.server.messaging.notification.domain.Device

data class DeviceResponse(
    val ownerId: Long,
    val token: String,
    val id: Long
) {
    
    companion object {

        fun of(device: Device) = DeviceResponse(device.ownerId, device.token, device.id)
    }
}
