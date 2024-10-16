package com.snackgame.server.messaging.notification.domain

import org.springframework.data.jpa.repository.JpaRepository

interface DeviceRepository : JpaRepository<Device, Long> {

    fun existsByOwnerIdAndToken(ownerId: Long, token: String): Boolean

    fun findAllByOwnerId(ownerId: Long): List<Device>
}
