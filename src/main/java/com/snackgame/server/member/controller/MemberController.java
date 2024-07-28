package com.snackgame.server.member.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.snackgame.server.auth.oauth.support.JustAuthenticated;
import com.snackgame.server.auth.token.TokenService;
import com.snackgame.server.auth.token.dto.TokensDto;
import com.snackgame.server.auth.token.support.Authenticated;
import com.snackgame.server.auth.token.support.TokenToCookies;
import com.snackgame.server.member.controller.dto.GroupRequest;
import com.snackgame.server.member.controller.dto.MemberDetailsResponse;
import com.snackgame.server.member.controller.dto.MemberRequest;
import com.snackgame.server.member.controller.dto.NameRequest;
import com.snackgame.server.member.domain.Member;
import com.snackgame.server.member.domain.SocialMember;
import com.snackgame.server.member.service.MemberAccountService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final TokenService tokenService;
    private final TokenToCookies tokenToCookies;
    private final MemberAccountService memberAccountService;

    @Operation(summary = "일반 사용자 생성", description = "이름, 그룹으로 사용자를 생성한다")
    @PostMapping("/members")
    public MemberDetailsResponse addMember(@Valid @RequestBody MemberRequest memberRequest) {
        Member added = memberAccountService.createWith(memberRequest.getName(), memberRequest.getGroup());
        return MemberDetailsResponse.of(added);
    }

    @Operation(summary = "모든 이름 검색", description = "인자로 시작하는 모든 사용자 이름을 가져온다")
    @GetMapping("/members/names")
    public List<String> showNamesStartWith(@RequestParam("startWith") String prefix) {
        return memberAccountService.findNamesStartWith(prefix);
    }

    @Operation(summary = "나의 정보", description = "현재 사용자의 정보를 받아온다")
    @GetMapping("/members/me")
    public MemberDetailsResponse showDetailsOf(@Authenticated Member member) {
        return MemberDetailsResponse.of(member);
    }

    @Operation(summary = "나의 이름 변경", description = "현재 사용자의 이름을 변경한다")
    @PutMapping("/members/me/name")
    public MemberDetailsResponse changeName(
            @Authenticated Member member,
            @RequestBody NameRequest nameRequest
    ) {
        memberAccountService.changeNameOf(member.getId(), nameRequest.getName());
        return MemberDetailsResponse.of(member);
    }

    @Operation(summary = "나의 그룹 지정", description = "현재 사용자의 그룹을 지정한다")
    @PutMapping("/members/me/group")
    public MemberDetailsResponse changeGroup(
            @Authenticated Member member,
            @RequestBody GroupRequest groupRequest
    ) {
        memberAccountService.changeGroupNameOf(member.getId(), groupRequest.getGroup());
        return MemberDetailsResponse.of(member);
    }

    @Operation(summary = "나의 프로필 이미지 변경", description = "현재 사용자의 프로필 이미지를 변경한다")
    @PutMapping(value = "/members/me/profile-image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public MemberDetailsResponse changeProfileImage(
            @Authenticated Member member,
            @RequestPart MultipartFile profileImage
    ) {
        memberAccountService.changeProfileImageOf(member.getId(), profileImage.getResource());
        return MemberDetailsResponse.of(member);
    }

    @Operation(summary = "현재 계정을 소셜 계정으로 통합",
            description = "현재 사용자를 직전에 로그인한 소셜 계정으로 통합한다")
    @PostMapping("/members/me/integrate")
    public ResponseEntity<MemberDetailsResponse> integrate(
            @Authenticated Member victim,
            @JustAuthenticated SocialMember socialMember
    ) {
        Member integrated = memberAccountService.integrate(victim.getId(), socialMember.getId());
        TokensDto tokens = tokenService.issueFor(integrated.getId());

        return ResponseEntity.ok()
                .header(SET_COOKIE, tokenToCookies.from(tokens))
                .body(MemberDetailsResponse.of(integrated));
    }
}
