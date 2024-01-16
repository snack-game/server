package com.snackgame.server.member.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.jwt.AccessTokenProvider;
import com.snackgame.server.auth.jwt.RefreshTokenProvider;
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
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    @Operation(summary = "게스트 토큰 발급", description = "임시 사용자를 생성하고, 토큰을 발급한다")
    @PostMapping("/tokens/guest")
    public MemberDetailsWithTokenResponse issueToken(HttpServletResponse response) {
        Member guest = memberService.createGuest();
        String accessToken = accessTokenProvider.createTokenWith(guest.getId().toString());
        String refreshToken = refreshTokenProvider.issue(guest.getId());
        sendRefreshTokenCookie(response, refreshToken);

        return MemberDetailsWithTokenResponse.of(guest, accessToken);
    }

    @Operation(summary = "일반 사용자 토큰 발급", description = "사용자의 이름으로 토큰을 발급한다")
    @PostMapping("/tokens")
    public MemberDetailsWithTokenResponse issueToken(@RequestBody NameRequest nameRequest,
            HttpServletResponse response) {
        Member found = memberService.getBy(nameRequest.getName());
        String accessToken = accessTokenProvider.createTokenWith(found.getId().toString());
        String refreshToken = refreshTokenProvider.issue(found.getId());
        sendRefreshTokenCookie(response, refreshToken);

        return MemberDetailsWithTokenResponse.of(found, accessToken);
    }

    @Operation(
            summary = "소셜 사용자 토큰 발급",
            description = "방금 소셜 로그인한 사용자의 토큰을 발급한다. "
                          + "로그인 시 사용했던 <b>SESSION ID를 포함</b>해야 한다."
    )
    @PostMapping("/tokens/social")
    public MemberDetailsWithTokenResponse issueToken(@JustAuthenticated SocialMember socialMember,
            HttpServletResponse response) {
        String token = accessTokenProvider.createTokenWith(socialMember.getId().toString());
        String refreshToken = refreshTokenProvider.issue(socialMember.getId());
        sendRefreshTokenCookie(response, refreshToken);

        return MemberDetailsWithTokenResponse.of(socialMember, token);
    }

    @Operation(summary = "토큰 재발급", description = "토큰을 재발급한다")
    @PostMapping("/tokens/reissue")
    public TokenResponse reissueToken(String refreshToken, HttpServletResponse response) {
        List<String> tokens = refreshTokenProvider.reissue(refreshToken);
        String accessToken = tokens.get(0);
        String newRefreshToken = tokens.get(1);
        sendRefreshTokenCookie(response, newRefreshToken);

        return new TokenResponse(accessToken);
    }

    private void sendRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(HttpHeaders.AUTHORIZATION, refreshToken);
        cookie.setMaxAge(2592000);
        cookie.setHttpOnly(true);
        cookie.setPath("/tokens/reissue");
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
