package com.snackgame.server.messaging.notification.domain

import org.springframework.data.jpa.repository.JpaRepository

interface DeviceRepository : JpaRepository<Device, Long> {

    fun findAllByOwnerId(ownerId: Long): List<Device>
}
