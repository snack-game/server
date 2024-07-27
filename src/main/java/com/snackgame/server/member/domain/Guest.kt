package com.snackgame.server.member.domain

import com.snackgame.server.member.exception.GuestRestrictedException
import javax.persistence.Entity

@Entity
class Guest(name: Name) : Member(name) {

    override fun changeNameTo(name: Name) {
        throw GuestRestrictedException()
    }

    override fun changeGroupTo(group: Group?) {
        throw GuestRestrictedException()
    }

    override val accountType: AccountType
        get() = AccountType.GUEST
}
