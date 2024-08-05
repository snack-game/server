package com.snackgame.server.member.service.dto

import com.snackgame.server.member.controller.dto.GroupResponse
import com.snackgame.server.member.controller.dto.StatusResponse
import com.snackgame.server.member.domain.AccountType
import com.snackgame.server.member.domain.Member
import io.swagger.v3.oas.annotations.media.Schema


class MemberDetailsResponse(
    @field:Schema(example = "1") val id: Long,
    @field:Schema(example = "닉네임")
    val name: String,
    val group: GroupResponse?,
    @field:Schema(example = "https://snackgame.s3.ap-northeast-2.amazonaws.com/unhashed/7d9b26272791438b8dc6893a4cbd6f50-77423374")
    val profileImage: String,
    val status: StatusResponse,
    @field:Schema(example = "SOCIAL", allowableValues = ["SELF", "GUEST", "SOCIAL"])
    val type: AccountType
) {

    companion object {
        fun of(member: Member): MemberDetailsResponse {
            return MemberDetailsResponse(
                member.id,
                member.getNameAsString(),
                GroupResponse.of(member.group),
                member.profileImage.url,
                StatusResponse.of(member.status),
                member.accountType
            )
        }
    }
}
