package com.snackgame.server.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.jwt.JwtProvider;
import com.snackgame.server.auth.oauth.support.JustAuthenticated;
import com.snackgame.server.member.business.MemberService;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.controller.dto.MemberDetailsWithTokenResponse;
import com.snackgame.server.member.controller.dto.NameRequest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "일반 사용자 토큰 발급", description = "사용자의 이름으로 토큰을 발급한다")
    @PostMapping("/tokens")
    public MemberDetailsWithTokenResponse issueToken(@RequestBody NameRequest nameRequest) {
        Member found = memberService.getBy(nameRequest.getName());
        String accessToken = jwtProvider.createTokenWith(found.getId().toString());
        return MemberDetailsWithTokenResponse.of(found, accessToken);
    }

    @Operation(
            summary = "소셜 사용자 토큰 발급",
            description = "방금 소셜 로그인한 사용자의 토큰을 발급한다. "
                          + "로그인 시 사용했던 <b>SESSION ID를 포함</b>해야 한다."
    )
    @PostMapping("/tokens/social")
    public MemberDetailsWithTokenResponse issueToken(
            @JustAuthenticated Member socialMember
    ) {
        String token = jwtProvider.createTokenWith(socialMember.getId().toString());
        return MemberDetailsWithTokenResponse.of(socialMember, token);
    }
}
