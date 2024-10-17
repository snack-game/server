package com.snackgame.server.messaging.notification.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["ownerId", "token"])])
class Device(
    val ownerId: Long,
    val token: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)
