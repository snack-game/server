package com.snackgame.server.member.service

import com.snackgame.server.auth.oauth.oidc.IdTokenResolver
import com.snackgame.server.auth.oauth.oidc.payload.IdTokenPayload
import com.snackgame.server.member.controller.dto.OidcRequest
import com.snackgame.server.member.domain.Member
import com.snackgame.server.member.domain.MemberRepository
import com.snackgame.server.member.domain.Name
import com.snackgame.server.member.domain.ProfileImage
import com.snackgame.server.member.domain.SocialMember
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Deprecated("멤버 서비스 코틀린 마이그레이션 시 통합 예정")
@Component
open class OidcMemberService(
    private val members: MemberRepository,
    private val distinctNaming: DistinctNaming,
    private val nameRandomizer: NameRandomizer,
    private val idTokenResolver: IdTokenResolver
) {

    @Transactional
    open fun getBy(oidcRequest: OidcRequest): Member {
        val payload: IdTokenPayload = idTokenResolver.resolve(oidcRequest.idToken)
        val socialMember = members.findByProviderAndProvidedId(payload.provider, payload.id)
            .orElseGet {
                SocialMember(
                    payload.distinctName,
                    payload.profileImage,
                    payload.provider,
                    payload.id
                )
            }
        socialMember.setAdditional(payload.email, payload.distinctName.string)
        return members.save(socialMember)
    }

    private val IdTokenPayload.distinctName: Name
        get() {
            if (this.name.isNullOrBlank()) {
                return distinctNaming.from(nameRandomizer.getWith(this.provider))
            }
            return distinctNaming.from(Name(this.name))
        }

    private val IdTokenPayload.profileImage: ProfileImage
        get() {
            if (this.picture.isNullOrBlank()) {
                return ProfileImage.EMPTY
            }
            return ProfileImage(picture)
        }
}
