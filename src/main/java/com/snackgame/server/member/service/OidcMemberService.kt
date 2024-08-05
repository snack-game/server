package com.snackgame.server.member.service

import com.snackgame.server.auth.oauth.oidc.IdTokenResolver
import com.snackgame.server.auth.oauth.oidc.payload.IdTokenPayload
import com.snackgame.server.member.controller.dto.OidcRequest
import com.snackgame.server.member.domain.Guest
import com.snackgame.server.member.domain.Member
import com.snackgame.server.member.domain.MemberRepository
import com.snackgame.server.member.domain.Name
import com.snackgame.server.member.domain.ProfileImage
import com.snackgame.server.member.domain.SocialMember
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Deprecated("멤버 서비스 코틀린 마이그레이션 시 통합 예정")
@Component
class OidcMemberService(
    private val members: MemberRepository,
    private val distinctNaming: DistinctNaming,
    private val nameRandomizer: SocialMemberNameRandomizer,
    private val idTokenResolver: IdTokenResolver,
    private val memberAccountService: MemberAccountService
) {

    @Transactional
    fun getBy(oidcRequest: OidcRequest, guest: Guest?): Member {
        val payload: IdTokenPayload = idTokenResolver.resolve(oidcRequest.idToken)
        val socialMember = members.findByProviderAndProvidedId(payload.provider, payload.id)
            .orElseGet {
                SocialMember(
                    payload.provider,
                    payload.id,
                    payload.distinctName,
                    payload.profileImage
                )
            }
        socialMember.setAdditional(payload.email, payload.name)
        return members.save(socialMember).also {
            if (guest != null) {
                memberAccountService.integrate(guest.id, socialMember.id)
            }
        }
    }

    private val IdTokenPayload.distinctName: Name
        get() = distinctNaming.from(nameRandomizer.getName())

    private val IdTokenPayload.profileImage: ProfileImage
        get() {
            if (this.picture.isNullOrBlank()) {
                return ProfileImage.EMPTY
            }
            return ProfileImage(picture)
        }
}
