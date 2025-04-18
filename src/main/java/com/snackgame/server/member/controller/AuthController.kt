package com.snackgame.server.member.controller

import com.snackgame.server.auth.oauth.support.JustAuthenticated
import com.snackgame.server.auth.token.TokenService
import com.snackgame.server.auth.token.dto.TokensDto
import com.snackgame.server.auth.token.support.OptionalGuest
import com.snackgame.server.auth.token.support.TokenToCookies
import com.snackgame.server.auth.token.support.TokensFromCookie
import com.snackgame.server.member.controller.dto.NameRequest
import com.snackgame.server.member.controller.dto.OidcRequest
import com.snackgame.server.member.controller.dto.TokenResponse
import com.snackgame.server.member.domain.Guest
import com.snackgame.server.member.domain.SocialMember
import com.snackgame.server.member.service.MemberAccountService
import com.snackgame.server.member.service.OidcMemberService
import com.snackgame.server.member.service.dto.MemberDetailsResponse
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

    /**
     * 게스트 토큰 발급
     *
     * 임시 사용자를 생성하고, 토큰을 발급한다
     */
    @PostMapping("/tokens/guest")
    fun issueToken(): ResponseEntity<MemberDetailsResponse> {
        val guest = memberAccountService.createGuest()
        val tokens = tokenService.issueFor(guest.id)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.from(tokens))
            .body(guest)
    }

    /**
     * 일반 사용자 토큰 발급
     *
     * 사용자의 이름으로 토큰을 발급한다
     */
    @PostMapping("/tokens")
    fun issueToken(@RequestBody nameRequest: NameRequest): ResponseEntity<MemberDetailsResponse> {
        val member = memberAccountService.getBy(nameRequest.name)
        val tokens = tokenService.issueFor(member.id)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.from(tokens))
            .body(member)
    }

    /**
     * 소셜 사용자 토큰 발급
     *
     * 방금 소셜 로그인한 사용자의 토큰을 발급한다.
     *
     * 로그인 시 사용했던 **SESSION ID를 포함**해야 한다.
     */
    @PostMapping("/tokens/social")
    fun issueToken(@JustAuthenticated socialMember: SocialMember): ResponseEntity<MemberDetailsResponse> {
        val tokens = tokenService.issueFor(socialMember.id)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.from(tokens))
            .body(MemberDetailsResponse.of(socialMember))
    }

    /**
     * 소셜 사용자 로그인(OIDC)
     *
     * OIDC를 통해 발급한 Id Token으로 소셜 로그인한다.
     *
     * 또한 현재 게스트 사용자라면, 소셜 사용자로 통합한다.
     */
    @PostMapping("/tokens/social-oidc")
    fun issueFor(
        @Valid @RequestBody oidcRequest: OidcRequest,
        @OptionalGuest guest: Guest?
    ): ResponseEntity<MemberDetailsResponse> {
        val member = oidcMemberService.getBy(oidcRequest, guest)
        val tokens = tokenService.issueFor(member.id)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.from(tokens))
            .body(member)
    }

    /**
     * 토큰 재발급
     *
     * 리프레시 토큰으로 토큰을 재발급한다.
     *
     * 리프레시 토큰도 만료되어 갱신 불가한 경우
     * `REFRESH_TOKEN_EXPIRED_EXCEPTION`을 뱉는다.
     */
    @PatchMapping("/tokens/me")
    fun reissueToken(@TokensFromCookie tokens: TokensDto): ResponseEntity<TokenResponse> {
        val reissued = tokenService.reissueFrom(tokens.refreshToken)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.from(reissued))
            .build()
    }

    /**
     * 로그아웃
     *
     * 로그아웃하고, 쿠키에 저장된 토큰을 제거한다
     */
    @DeleteMapping("/tokens/me")
    fun logout(@TokensFromCookie tokens: TokensDto): ResponseEntity<Void> {
        tokenService.delete(tokens.refreshToken)

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, *tokenToCookies.empty())
            .build()
    }
}
