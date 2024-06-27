package com.snackgame.server.member.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.oauth.support.JustAuthenticated;
import com.snackgame.server.auth.token.TokenService;
import com.snackgame.server.auth.token.dto.TokensDto;
import com.snackgame.server.auth.token.support.TokenToCookies;
import com.snackgame.server.auth.token.support.TokensFromCookie;
import com.snackgame.server.member.controller.dto.MemberDetailsResponse;
import com.snackgame.server.member.controller.dto.NameRequest;
import com.snackgame.server.member.controller.dto.OidcRequest;
import com.snackgame.server.member.controller.dto.TokenResponse;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.SocialMember;
import com.snackgame.server.member.service.MemberAccountService;
import com.snackgame.server.member.service.OidcMemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final TokenToCookies tokenToCookies;
    private final TokenService tokenService;
    private final MemberAccountService memberAccountService;
    private final OidcMemberService oidcMemberService;

    @Operation(summary = "게스트 토큰 발급", description = "임시 사용자를 생성하고, 토큰을 발급한다")
    @PostMapping("/tokens/guest")
    public ResponseEntity<MemberDetailsResponse> issueToken() {
        Member guest = memberAccountService.createGuest();
        TokensDto tokens = tokenService.issueFor(guest.getId());

        return ResponseEntity.ok()
                .header(SET_COOKIE, tokenToCookies.from(tokens))
                .body(MemberDetailsResponse.of(guest));
    }

    @Operation(summary = "일반 사용자 토큰 발급", description = "사용자의 이름으로 토큰을 발급한다")
    @PostMapping("/tokens")
    public ResponseEntity<MemberDetailsResponse> issueToken(@RequestBody NameRequest nameRequest) {
        Member member = memberAccountService.getBy(nameRequest.getName());
        TokensDto tokens = tokenService.issueFor(member.getId());

        return ResponseEntity.ok()
                .header(SET_COOKIE, tokenToCookies.from(tokens))
                .body(MemberDetailsResponse.of(member));
    }

    @Operation(
            summary = "소셜 사용자 토큰 발급",
            description = "방금 소셜 로그인한 사용자의 토큰을 발급한다. "
                          + "로그인 시 사용했던 <b>SESSION ID를 포함</b>해야 한다."
    )
    @PostMapping("/tokens/social")
    public ResponseEntity<MemberDetailsResponse> issueToken(@JustAuthenticated SocialMember socialMember) {
        TokensDto tokens = tokenService.issueFor(socialMember.getId());

        return ResponseEntity.ok()
                .header(SET_COOKIE, tokenToCookies.from(tokens))
                .body(MemberDetailsResponse.of(socialMember));
    }

    @Operation(summary = "소셜 사용자 로그인(OIDC)", description = "OIDC를 통해 발급한 Id Token를 사용해 소셜 로그인한다.\n\n"
                                                           + "OAuth Scope는 `email`, `profile`, `openid`여야 하며, \n\n"
                                                           + "서비스 인증정보는 쿠키에 저장된다."
    )
    @PostMapping("/tokens/social-oidc")
    public ResponseEntity<MemberDetailsResponse> issueFor(@Valid @RequestBody OidcRequest oidcRequest) {
        Member member = oidcMemberService.getBy(oidcRequest);
        TokensDto tokens = tokenService.issueFor(member.getId());

        return ResponseEntity.ok()
                .header(SET_COOKIE, tokenToCookies.from(tokens))
                .body(MemberDetailsResponse.of(member));
    }

    @Operation(
            summary = "토큰 재발급",
            description = "리프레시 토큰으로 토큰을 재발급한다\n\n"
                          + "**각 상황에 대한 응답은 하단 참고**",
            responses = {
                    @ApiResponse(
                            responseCode = "401",
                            description = "액세스 토큰이 만료되었을 때, 인증이 필요한 API를 호출한 경우",
                            content = @Content(examples = @ExampleObject(
                                    "{\n"
                                    + "  \"action\": \"REISSUE\",\n"
                                    + "  \"messages\": [\n"
                                    + "    \"토큰이 만료되었습니다\"\n"
                                    + "  ]\n"
                                    + "}"
                            ))
                    ),
                    @ApiResponse(
                            responseCode = "401 ",
                            description = "리프레시 토큰도 만료되었을 때, 인증이 필요한 API를 호출한 경우",
                            content = @Content(examples = @ExampleObject(
                                    "{\n"
                                    + "  \"action\": null,\n"
                                    + "  \"messages\": [\n"
                                    + "    \"토큰을 읽지 못했습니다\"\n"
                                    + "  ]\n"
                                    + "}"
                            ))
                    )
            }
    )
    @PatchMapping("/tokens/me")
    public ResponseEntity<TokenResponse> reissueToken(@TokensFromCookie TokensDto tokens) {
        TokensDto reissued = tokenService.reissueFrom(tokens.getRefreshToken());

        return ResponseEntity.ok()
                .header(SET_COOKIE, tokenToCookies.from(reissued))
                .body(new TokenResponse(reissued.getAccessToken()));
    }

    @Operation(
            summary = "로그아웃",
            description = "쿠키에 저장된 토큰을 제거한다"
    )
    @DeleteMapping("/tokens/me")
    public ResponseEntity<Void> logout(@TokensFromCookie TokensDto tokens) {
        tokenService.delete(tokens.getRefreshToken());

        return ResponseEntity.ok()
                .header(SET_COOKIE, tokenToCookies.empty())
                .build();
    }
}
