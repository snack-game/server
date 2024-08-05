package com.snackgame.server.member.service

import com.snackgame.server.common.file.ResourceResolver
import com.snackgame.server.member.service.dto.MemberDetailsResponse
import com.snackgame.server.member.domain.Guest
import com.snackgame.server.member.domain.Member
import com.snackgame.server.member.domain.MemberRepository
import com.snackgame.server.member.domain.Name
import com.snackgame.server.member.domain.ProfileImage
import com.snackgame.server.member.domain.getBy
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberAccountService(
    private val members: MemberRepository,
    private val groupService: GroupService,
    private val distinctNaming: DistinctNaming,
    private val accountIntegrations: List<AccountIntegration>,

    private val resourceResolver: ResourceResolver
) {

    @Transactional
    fun createWith(name: String): MemberDetailsResponse = createWith(name, null)

    @Transactional
    fun createWith(name: String, groupName: String?): MemberDetailsResponse {
        val newName = Name(name)
            .also { distinctNaming.validate(it) }

        val newMember = Member(newName)
        if (groupName != null) {
            newMember.changeGroupTo(groupService.createIfNotExists(groupName))
        }
        return MemberDetailsResponse.of(members.save(newMember))
    }

    @Transactional
    fun createGuest(): MemberDetailsResponse {
        val guest = Guest(distinctNaming.ofGuest())
        return MemberDetailsResponse.of(members.save(guest))
    }

    // TODO: 컴포넌트로 분리
    @Transactional
    fun integrate(victimId: Long, currentMemberId: Long): Member {
        for (integration in accountIntegrations) {
            integration.execute(victimId, currentMemberId)
        }
        members.deleteById(victimId)
        return members.getById(currentMemberId)
    }

    @Transactional
    fun changeNameOf(memberId: Long, name: String) {
        val member = members.getById(memberId)
        val otherName = Name(name)
        distinctNaming.validate(otherName)
        member.changeNameTo(otherName)
    }

    @Transactional
    fun changeGroupNameOf(memberId: Long, groupName: String) {
        val member = members.getById(memberId)
        val group = groupService.createIfNotExists(groupName)
        member.changeGroupTo(group)
    }

    @Transactional
    fun changeProfileImageOf(memberId: Long, resource: Resource) {
        val member = members.getById(memberId)
        val resolved = resourceResolver.resolve(resource)
        member.changeProfileImageTo(ProfileImage(resolved.toString()))
    }

    fun getBy(id: Long): MemberDetailsResponse = MemberDetailsResponse.of(members.getBy(id))

    fun getBy(name: String): MemberDetailsResponse = MemberDetailsResponse.of(members.getBy(Name(name)))

    fun findNamesStartWith(prefix: String): List<String> {
        return members.findByNameStringStartingWith(prefix)
            .map { it.getNameAsString() }
    }
}
