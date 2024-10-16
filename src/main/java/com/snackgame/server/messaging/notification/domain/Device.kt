package com.snackgame.server.messaging.notification.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Device(
    val ownerId: Long,
    val token: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)
