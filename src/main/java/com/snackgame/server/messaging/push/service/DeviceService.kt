package com.snackgame.server.messaging.push.service

import com.snackgame.server.messaging.push.domain.Device
import com.snackgame.server.messaging.push.domain.DeviceRepository
import com.snackgame.server.messaging.push.exception.DuplicatedDeviceException
import com.snackgame.server.messaging.push.service.dto.DeviceResponse
import com.snackgame.server.messaging.push.service.dto.DeviceTokenRequest
import org.springframework.stereotype.Service

@Service
class DeviceService(private val deviceRepository: DeviceRepository) {

    fun registerDeviceFor(ownerId: Long, deviceToken: DeviceTokenRequest) {
        if (!deviceRepository.existsByOwnerIdAndToken(ownerId, deviceToken.token)) {
            Device(ownerId, deviceToken.token)
                .let { deviceRepository.save(it) }
            return
        }
        throw DuplicatedDeviceException()
    }

    fun getDevicesOf(ownerId: Long): List<DeviceResponse> {
        return deviceRepository.findAllByOwnerId(ownerId)
            .map { DeviceResponse.of(it) }
    }
}
