package com.snackgame.server.member.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.oauth.support.JustAuthenticated;
import com.snackgame.server.auth.token.TokenService;
import com.snackgame.server.auth.token.dto.TokenDto;
import com.snackgame.server.member.MemberService;
import com.snackgame.server.member.controller.dto.MemberDetailsWithTokenResponse;
import com.snackgame.server.member.controller.dto.NameRequest;
import com.snackgame.server.member.controller.dto.TokenResponse;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.SocialMember;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private final MemberService memberService;
    private final TokenService tokenService;

    @Operation(summary = "게스트 토큰 발급", description = "임시 사용자를 생성하고, 토큰을 발급한다")
    @PostMapping("/tokens/guest")
    public ResponseEntity<MemberDetailsWithTokenResponse> issueToken() {
        Member guest = memberService.createGuest();
        TokenDto token = tokenService.issueFor(guest.getId());

        return responseWith(token)
                .body(MemberDetailsWithTokenResponse.of(guest, token.getAccessToken()));
    }

    @Operation(summary = "일반 사용자 토큰 발급", description = "사용자의 이름으로 토큰을 발급한다")
    @PostMapping("/tokens")
    public ResponseEntity<MemberDetailsWithTokenResponse> issueToken(@RequestBody NameRequest nameRequest) {
        Member member = memberService.getBy(nameRequest.getName());
        TokenDto token = tokenService.issueFor(member.getId());

        return responseWith(token)
                .body(MemberDetailsWithTokenResponse.of(member, token.getAccessToken()));
    }

    @Operation(
            summary = "소셜 사용자 토큰 발급",
            description = "방금 소셜 로그인한 사용자의 토큰을 발급한다. "
                          + "로그인 시 사용했던 <b>SESSION ID를 포함</b>해야 한다."
    )
    @PostMapping("/tokens/social")
    public ResponseEntity<MemberDetailsWithTokenResponse> issueToken(@JustAuthenticated SocialMember socialMember) {
        TokenDto token = tokenService.issueFor(socialMember.getId());

        return responseWith(token)
                .body(MemberDetailsWithTokenResponse.of(socialMember, token.getAccessToken()));
    }

    @Operation(
            summary = "토큰 재발급",
            description = "리프레시 토큰으로 토큰을 재발급한다\n\n"
                          + "**예외 조치 종류**\n\n"
                          + "`REISSUE`: 액세스 토큰 재발급이 필요하다\n\n"
                          + "`LOGOUT`: 리프레시 토큰이 만료되어 토큰 재발급이 불가, 로그아웃 해야한다"
    )
    @PostMapping("/tokens/reissue")
    public ResponseEntity<TokenResponse> reissueToken(@CookieValue(REFRESH_TOKEN_COOKIE_NAME) String refreshToken) {
        TokenDto reissued = tokenService.reissueFrom(refreshToken);

        return responseWith(reissued)
                .body(new TokenResponse(reissued.getAccessToken()));
    }

    private ResponseEntity.BodyBuilder responseWith(TokenDto token) {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token.getRefreshToken())
                                .path("/tokens/reissue")
                                .maxAge(token.getRefreshTokenExpiry())
                                .httpOnly(true)
                                .secure(true)
                                .build()
                                .toString()
                );
    }
}
