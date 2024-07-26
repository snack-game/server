package com.snackgame.server.member.controller

import com.snackgame.server.auth.oauth.support.JustAuthenticated
import com.snackgame.server.auth.token.TokenService
import com.snackgame.server.auth.token.dto.TokensDto
import com.snackgame.server.auth.token.support.OptionalGuest
import com.snackgame.server.auth.token.support.TokenToCookies
import com.snackgame.server.auth.token.support.TokensFromCookie
import com.snackgame.server.member.controller.dto.MemberDetailsResponse
import com.snackgame.server.member.controller.dto.NameRequest
import com.snackgame.server.member.controller.dto.OidcRequest
import com.snackgame.server.member.controller.dto.TokenResponse
import com.snackgame.server.member.domain.Guest
import com.snackgame.server.member.domain.SocialMember
import com.snackgame.server.member.service.MemberAccountService
import com.snackgame.server.member.service.OidcMemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AuthController(
    private val tokenToCookies: TokenToCookies,
    private val tokenService: TokenService,
    private val memberAccountService: MemberAccountService,
    private val oidcMemberService: OidcMemberService
) {

    @Operation(summary = "게스트 토큰 발급", description = "임시 사용자를 생성하고, 토큰을 발급한다")
    @PostMapping("/tokens/guest")
    fun issueToken(): ResponseEntity<MemberDetailsResponse> {
        val guest = memberAccountService.createGuest()
        val tokens = tokenService.issueFor(guest.id)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.from(tokens))
            .body(MemberDetailsResponse.of(guest))
    }

    @Operation(summary = "일반 사용자 토큰 발급", description = "사용자의 이름으로 토큰을 발급한다")
    @PostMapping("/tokens")
    fun issueToken(@RequestBody nameRequest: NameRequest): ResponseEntity<MemberDetailsResponse> {
        val member = memberAccountService.getBy(nameRequest.name)
        val tokens = tokenService.issueFor(member.id)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.from(tokens))
            .body(MemberDetailsResponse.of(member))
    }

    @Operation(
        summary = "소셜 사용자 토큰 발급", description = "방금 소셜 로그인한 사용자의 토큰을 발급한다. " + "로그인 시 사용했던 <b>SESSION ID를 포함</b>해야 한다."
    )
    @PostMapping("/tokens/social")
    fun issueToken(@JustAuthenticated socialMember: SocialMember): ResponseEntity<MemberDetailsResponse> {
        val tokens = tokenService.issueFor(socialMember.id)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.from(tokens))
            .body(MemberDetailsResponse.of(socialMember))
    }

    @Operation(
        summary = "소셜 사용자 로그인(OIDC)", description = """OIDC를 통해 발급한 Id Token으로 소셜 로그인한다.

현재 게스트 사용자라면 **소셜 사용자로 통합**한다"""
    )
    @PostMapping("/tokens/social-oidc")
    fun issueFor(
        @Valid @RequestBody oidcRequest: OidcRequest,
        @OptionalGuest guest: Guest?
    ): ResponseEntity<MemberDetailsResponse> {
        val member = oidcMemberService.getBy(oidcRequest, guest)
        val tokens = tokenService.issueFor(member.id)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.from(tokens))
            .body(MemberDetailsResponse.of(member))
    }

    @Operation(
        summary = "토큰 재발급", description = """리프레시 토큰으로 토큰을 재발급한다

**각 상황에 대한 응답은 하단 참고**""", responses = [
            ApiResponse(
                responseCode = "401", description = "만료된 액세스 토큰으로 인증이 필요한 API를 호출한 경우", content = [Content(
                    examples = [ExampleObject(
                        """{
                          "code": "TOKEN_EXPIRED_EXCEPTION",
                          "messages": [
                            "토큰이 만료되었습니다"
                          ]
                        }"""
                    )]
                )]
            ),
            ApiResponse(
                responseCode = "401 ", description = "리프레시 토큰도 만료되어 갱신 불가한 경우", content = [Content(
                    examples = [ExampleObject(
                        """{
                          "code": "REFRESH_TOKEN_EXPIRED_EXCEPTION",
                          "messages": [
                            "리프레시 토큰도 만료되었습니다"
                          ]
                        }"""
                    )]
                )]
            )]
    )
    @PatchMapping("/tokens/me")
    fun reissueToken(@TokensFromCookie tokens: TokensDto): ResponseEntity<TokenResponse> {
        val reissued = tokenService.reissueFrom(tokens.refreshToken)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.from(reissued))
            .build()
    }

    @Operation(summary = "로그아웃", description = "쿠키에 저장된 토큰을 제거한다")
    @DeleteMapping("/tokens/me")
    fun logout(@TokensFromCookie tokens: TokensDto): ResponseEntity<Void> {
        tokenService.delete(tokens.refreshToken)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.empty())
            .build()
    }
}
