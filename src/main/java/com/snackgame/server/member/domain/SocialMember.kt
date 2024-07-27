package com.snackgame.server.member.domain

import javax.persistence.Entity

@Entity
class SocialMember(
    val provider: String,
    val providedId: String,
    name: Name,
    profileImage: ProfileImage = ProfileImage.EMPTY,
    group: Group? = null,
    id: Long = 0,
) : Member(name, group, profileImage, id) {

    private var email: String? = null
    private var nickname: String? = null

    fun setAdditional(email: String?, nickname: String?) {
        this.email = email
        this.nickname = nickname
    }

    override val accountType: AccountType
        get() = AccountType.SOCIAL
}
