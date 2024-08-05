package com.snackgame.server.member.domain

import com.snackgame.server.member.exception.MemberNotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository : JpaRepository<Member, Long?> {

    @Query("select g from Guest g where g.id = :id")
    fun findGuestById(id: Long): Guest?

    fun findByName(name: Name): Member?

    fun existsByName(name: Name?): Boolean

    fun findByNameStringStartingWith(prefix: String?): List<Member>

    fun findByProviderAndProvidedId(provider: String, providedId: String): Optional<SocialMember>
}

fun MemberRepository.getBy(id: Long): Member =
    findByIdOrNull(id) ?: throw MemberNotFoundException()

fun MemberRepository.getBy(name: Name): Member =
    findByName(name) ?: throw MemberNotFoundException()

