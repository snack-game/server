package com.snackgame.server.member.controller

import com.snackgame.server.auth.oauth.support.JustAuthenticated
import com.snackgame.server.auth.token.TokenService
import com.snackgame.server.auth.token.support.Authenticated
import com.snackgame.server.auth.token.support.TokenToCookies
import com.snackgame.server.member.controller.dto.NameRequest
import com.snackgame.server.member.domain.Member
import com.snackgame.server.member.domain.SocialMember
import com.snackgame.server.member.service.MemberAccountService
import com.snackgame.server.member.service.dto.GroupRequest
import com.snackgame.server.member.service.dto.MemberDetailsResponse
import com.snackgame.server.member.service.dto.MemberRequest
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
class MemberController(
    private val tokenService: TokenService,
    private val tokenToCookies: TokenToCookies,
    private val memberAccountService: MemberAccountService
) {

    @Operation(summary = "일반 사용자 생성", description = "이름, 그룹으로 사용자를 생성한다")
    @PostMapping("/members")
    fun addMember(@Valid @RequestBody memberRequest: MemberRequest): MemberDetailsResponse {
        return memberAccountService.createWith(memberRequest.name, memberRequest.group)
    }

    @Operation(summary = "모든 이름 검색", description = "인자로 시작하는 모든 사용자 이름을 가져온다")
    @GetMapping("/members/names")
    fun showNamesStartWith(@RequestParam("startWith") prefix: String): List<String> {
        return memberAccountService.findNamesStartWith(prefix)
    }

    @Operation(summary = "나의 정보", description = "현재 사용자의 정보를 받아온다")
    @GetMapping("/members/me")
    fun showDetailsOf(@Authenticated member: Member): MemberDetailsResponse {
        return MemberDetailsResponse.of(member)
    }

    @Operation(summary = "나의 이름 변경", description = "현재 사용자의 이름을 변경한다")
    @PutMapping("/members/me/name")
    fun changeName(
        @Authenticated member: Member,
        @Valid @RequestBody nameRequest: NameRequest
    ): MemberDetailsResponse {
        memberAccountService.changeNameOf(member.id, nameRequest.name)
        return MemberDetailsResponse.of(member)
    }

    @Operation(summary = "나의 그룹 지정", description = "현재 사용자의 그룹을 지정한다")
    @PutMapping("/members/me/group")
    fun changeGroup(
        @Authenticated member: Member,
        @Valid @RequestBody groupRequest: GroupRequest
    ): MemberDetailsResponse {
        memberAccountService.changeGroupNameOf(member.id, groupRequest.group)
        return MemberDetailsResponse.of(member)
    }

    @Operation(summary = "나의 프로필 이미지 변경", description = "현재 사용자의 프로필 이미지를 변경한다")
    @PutMapping(value = ["/members/me/profile-image"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun changeProfileImage(
        @Authenticated member: Member,
        @RequestPart profileImage: MultipartFile
    ): MemberDetailsResponse {
        memberAccountService.changeProfileImageOf(member.id, profileImage.resource)
        return MemberDetailsResponse.of(member)
    }

    @Operation(summary = "현재 사용자 계정 탈퇴", description = "현재 사용자의 계정을 탈퇴한다")
    @DeleteMapping(value = ["/members/me"])
    fun delete(@Authenticated member: Member) {
        memberAccountService.delete(member.id)
    }

    @Operation(summary = "현재 계정을 소셜 계정으로 통합", description = "현재 사용자를 직전에 로그인한 소셜 계정으로 통합한다")
    @PostMapping("/members/me/integrate")
    fun integrate(
        @Authenticated victim: Member,
        @JustAuthenticated socialMember: SocialMember
    ): ResponseEntity<MemberDetailsResponse> {
        val integrated = memberAccountService.integrate(victim.id, socialMember.id)
        val tokens = tokenService.issueFor(integrated.id)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.from(tokens))
            .body(MemberDetailsResponse.of(integrated))
    }
}
