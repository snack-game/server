package com.snackgame.server.member.service

import com.snackgame.server.auth.oauth.attributes.OAuthAttributes
import com.snackgame.server.auth.oauth.support.SocialMemberSaver
import com.snackgame.server.member.domain.MemberRepository
import com.snackgame.server.member.domain.ProfileImage
import com.snackgame.server.member.domain.SocialMember
import org.springframework.stereotype.Component

@Component
class SnackgameSocialMemberSaver(
    private val members: MemberRepository,
    private val distinctNaming: DistinctNaming,
    private val nameRandomizer: SocialMemberNameRandomizer
) : SocialMemberSaver<SocialMember> {

    override fun saveMemberFrom(attributes: OAuthAttributes): SocialMember {
        val member = members.findByProviderAndProvidedId(attributes.provider, attributes.id)
            .orElseGet {
                SocialMember(
                    attributes.provider,
                    attributes.id,
                    distinctNaming.from(nameRandomizer.getName()),
                    ProfileImage(attributes.pictureUrl)
                )
            }
        member.setAdditional(attributes.email, attributes.nickname)
        return members.save(member)
    }
}
