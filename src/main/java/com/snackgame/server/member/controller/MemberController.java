package com.snackgame.server.member.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.oauth.support.JustAuthenticated;
import com.snackgame.server.auth.token.support.Authenticated;
import com.snackgame.server.auth.token.util.JwtProvider;
import com.snackgame.server.member.MemberService;
import com.snackgame.server.member.controller.dto.GroupRequest;
import com.snackgame.server.member.controller.dto.MemberDetailsResponse;
import com.snackgame.server.member.controller.dto.MemberDetailsWithTokenResponse;
import com.snackgame.server.member.controller.dto.MemberRequest;
import com.snackgame.server.member.controller.dto.NameRequest;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.SocialMember;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider accessTokenProvider;

    @Operation(summary = "일반 사용자 생성", description = "이름, 그룹으로 사용자를 생성한다")
    @PostMapping("/members")
    public MemberDetailsWithTokenResponse addMember(@Valid @RequestBody MemberRequest memberRequest) {
        Member added = memberService.createWith(memberRequest.getName(), memberRequest.getGroup());
        String accessToken = accessTokenProvider.createTokenWith(added.getId().toString());
        return MemberDetailsWithTokenResponse.of(added, accessToken);
    }

    @Operation(summary = "모든 이름 검색", description = "인자로 시작하는 모든 사용자 이름을 가져온다")
    @GetMapping("/members/names")
    public List<String> showNamesStartWith(@RequestParam("startWith") String prefix) {
        return memberService.findNamesStartWith(prefix);
    }

    @Operation(summary = "나의 정보", description = "현재 사용자의 정보를 받아온다")
    @GetMapping("/members/me")
    public MemberDetailsResponse showDetailsOf(@Authenticated Member member) {
        return MemberDetailsResponse.of(member);
    }

    @Operation(summary = "나의 그룹 지정", description = "현재 사용자의 그룹을 지정한다")
    @PutMapping("/members/me/group")
    public void changeGroup(@Authenticated Member member, @RequestBody GroupRequest groupRequest) {
        memberService.changeGroupNameOf(member.getId(), groupRequest.getGroup());
    }

    @Operation(summary = "나의 이름 변경", description = "현재 사용자의 이름을 변경한다")
    @PutMapping("/members/me/name")
    public void changeName(@Authenticated Member member, @RequestBody NameRequest nameRequest) {
        memberService.changeNameOf(member.getId(), nameRequest.getName());
    }

    @Operation(
            summary = "현재 계정을 소셜 계정에 통합",
            description = "현재 사용자를 소셜 계정으로 통합하고, 토큰을 발급한다.<br>"
                          + "<b>직전에 소셜 로그인을 수행</b>해야 하고, 이 때 사용했던 <b>SESSION ID를 포함</b>해야 한다."
    )
    @PostMapping("/members/me/integrate")
    public MemberDetailsWithTokenResponse integrate(
            @Authenticated Member victim,
            @JustAuthenticated SocialMember socialMember
    ) {
        Member integrated = memberService.integrate(victim, socialMember);
        String token = accessTokenProvider.createTokenWith(integrated.getId().toString());
        return MemberDetailsWithTokenResponse.of(integrated, token);
    }
}
