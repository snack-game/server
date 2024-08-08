package com.snackgame.server.member.service

import com.snackgame.server.auth.token.support.GuestResolver
import com.snackgame.server.member.domain.Guest
import com.snackgame.server.member.domain.MemberRepository
import org.springframework.stereotype.Component

@Component
class SnackgameGuestResolver(private val members: MemberRepository) : GuestResolver<Guest?> {

    override fun resolve(guestId: Long): Guest? = members.findGuestById(guestId)
}
