package com.snackgame.server.member.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.jwt.TokenService;
import com.snackgame.server.auth.oauth.support.JustAuthenticated;
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

    private final MemberService memberService;
    private final TokenService tokenService;

    @Operation(summary = "게스트 토큰 발급", description = "임시 사용자를 생성하고, 토큰을 발급한다")
    @PostMapping("/tokens/guest")
    public ResponseEntity<MemberDetailsWithTokenResponse> issueToken() {
        Member guest = memberService.createGuest();
        TokenService.TokenDto token = tokenService.issueFor(guest.getId());

        return responseWith(token.getRefreshToken())
                .body(MemberDetailsWithTokenResponse.of(guest, token.getAccessToken()));
    }

    @Operation(summary = "일반 사용자 토큰 발급", description = "사용자의 이름으로 토큰을 발급한다")
    @PostMapping("/tokens")
    public ResponseEntity<MemberDetailsWithTokenResponse> issueToken(@RequestBody NameRequest nameRequest) {
        Member member = memberService.getBy(nameRequest.getName());
        TokenService.TokenDto token = tokenService.issueFor(member.getId());

        return responseWith(token.getRefreshToken())
                .body(MemberDetailsWithTokenResponse.of(member, token.getAccessToken()));
    }

    @Operation(
            summary = "소셜 사용자 토큰 발급",
            description = "방금 소셜 로그인한 사용자의 토큰을 발급한다. "
                          + "로그인 시 사용했던 <b>SESSION ID를 포함</b>해야 한다."
    )
    @PostMapping("/tokens/social")
    public ResponseEntity<MemberDetailsWithTokenResponse> issueToken(@JustAuthenticated SocialMember socialMember) {
        TokenService.TokenDto token = tokenService.issueFor(socialMember.getId());

        return responseWith(token.getRefreshToken())
                .body(MemberDetailsWithTokenResponse.of(socialMember, token.getAccessToken()));
    }

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급한다")
    @PostMapping("/tokens/reissue")
    public ResponseEntity<TokenResponse> reissueToken(@CookieValue("refreshToken") String refreshToken) {
        TokenService.TokenDto reissued = tokenService.reissueFrom(refreshToken);

        return responseWith(reissued.getRefreshToken())
                .body(new TokenResponse(reissued.getAccessToken()));
    }

    private ResponseEntity.BodyBuilder responseWith(String refreshToken) {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        ResponseCookie.from("refreshToken", refreshToken)
                                .path("/tokens/reissue")
                                .maxAge(Duration.ofDays(30))
                                .httpOnly(true)
                                .secure(true)
                                .build()
                                .toString()
                );
    }
}
