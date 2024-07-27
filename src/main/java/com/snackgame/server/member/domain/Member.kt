package com.snackgame.server.member.domain

import com.snackgame.server.common.domain.BaseEntity
import javax.persistence.DiscriminatorColumn
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Inheritance
import javax.persistence.ManyToOne

@EntityListeners(MemberEntityListener::class)
@Inheritance
@DiscriminatorColumn(name = "type")
@Entity
open class Member(
    name: Name,
    group: Group? = null,
    profileImage: ProfileImage = ProfileImage.EMPTY,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
) : BaseEntity() {

    @Embedded
    var name: Name = name
        private set

    @ManyToOne
    var group: Group? = group
        private set

    @Embedded
    var profileImage: ProfileImage = profileImage
        private set

    @Embedded
    val status: Status = Status()

    private var isValid = true

    @Deprecated("Java 하위 호환성 확보용, 코틀린 전환 완료 시 제거")
    constructor(name: Name) : this(name, null)

    open fun changeNameTo(name: Name) {
        this.name = name
    }

    open fun changeGroupTo(group: Group?) {
        this.group = group
    }

    fun changeProfileImageTo(profileImage: ProfileImage) {
        this.profileImage = profileImage
    }

    fun invalidate() {
        this.isValid = false
    }

    fun getNameAsString(): String = name.string

    open val accountType: AccountType
        get() = AccountType.SELF
}
