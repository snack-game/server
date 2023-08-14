package com.snackgame.server.member.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.auth.JwtProvider;
import com.snackgame.server.member.business.MemberService;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.controller.dto.GroupRequest;
import com.snackgame.server.member.controller.dto.MemberDetailsResponse;
import com.snackgame.server.member.controller.dto.MemberDetailsWithTokenResponse;
import com.snackgame.server.member.controller.dto.MemberRequest;
import com.snackgame.server.member.controller.dto.NameRequest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "일반 사용자 생성", description = "이름, 그룹으로 사용자를 생성한다")
    @PostMapping("/members")
    public MemberDetailsWithTokenResponse addMember(@RequestBody MemberRequest memberRequest) {
        Member added = memberService.createWith(memberRequest.getName(), memberRequest.getGroup());
        String accessToken = jwtProvider.createTokenWith(added.getId().toString());
        return MemberDetailsWithTokenResponse.of(added, accessToken);
    }

    @Operation(summary = "게스트로 사용자 생성", description = "추가정보 없이 임시 사용자를 생성한다")
    @PostMapping("/members/guest")
    public MemberDetailsWithTokenResponse addGuest() {
        Member guest = memberService.createGuest();
        String accessToken = jwtProvider.createTokenWith(guest.getId().toString());
        return MemberDetailsWithTokenResponse.of(guest, accessToken);
    }

    @Operation(summary = "사용자의 토큰 발급", description = "어떤 이름을 가진 사용자의 토큰을 발급한다")
    @PostMapping("/members/token")
    public MemberDetailsWithTokenResponse issueToken(@RequestBody NameRequest nameRequest) {
        Member found = memberService.findBy(nameRequest.getName());
        String accessToken = jwtProvider.createTokenWith(found.getId().toString());
        return MemberDetailsWithTokenResponse.of(found, accessToken);
    }

    @Operation(summary = "모든 이름 검색", description = "인자로 시작하는 모든 사용자 이름을 가져온다")
    @GetMapping("/members/names")
    public List<String> showNamesStartWith(@RequestParam("startWith") String prefix) {
        return memberService.findNamesStartWith(prefix);
    }

    @Operation(summary = "나의 정보", description = "현재 사용자의 정보를 받아온다")
    @GetMapping("/members/me")
    public MemberDetailsResponse showDetailsOf(Member member) {
        return MemberDetailsResponse.of(member);
    }

    @Operation(summary = "나의 그룹 지정", description = "현재 사용자의 그룹을 지정한다")
    @PutMapping("/members/me/group")
    public void changeGroup(Member member, @RequestBody GroupRequest groupRequest) {
        memberService.changeGroupNameOf(member, groupRequest.getGroup());
    }

    @Operation(summary = "나의 이름 변경", description = "현재 사용자의 이름을 변경한다")
    @PutMapping("/members/me/name")
    public void changeName(Member member, @RequestBody NameRequest nameRequest) {
        memberService.changeNameOf(member, nameRequest.getName());
    }
}
