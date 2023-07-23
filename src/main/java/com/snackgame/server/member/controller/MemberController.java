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

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @PostMapping("/members")
    public MemberDetailsWithTokenResponse addMember(@RequestBody MemberRequest memberRequest) {
        Member added = memberService.createWith(memberRequest.getName(), memberRequest.getGroup());
        String accessToken = jwtProvider.createTokenWith(added.getId().toString());
        return MemberDetailsWithTokenResponse.of(added, accessToken);
    }

    @PostMapping("/members/guest")
    public MemberDetailsWithTokenResponse addGuest() {
        Member guest = memberService.createGuest();
        String accessToken = jwtProvider.createTokenWith(guest.getId().toString());
        return MemberDetailsWithTokenResponse.of(guest, accessToken);
    }

    @PostMapping("/members/token")
    public MemberDetailsWithTokenResponse issueToken(@RequestBody NameRequest nameRequest) {
        Member found = memberService.findBy(nameRequest.getName());
        String accessToken = jwtProvider.createTokenWith(found.getId().toString());
        return MemberDetailsWithTokenResponse.of(found, accessToken);
    }

    @GetMapping("/members/names")
    public List<String> showNamesStartWith(@RequestParam("startWith") String prefix) {
        return memberService.findNamesStartWith(prefix);
    }

    @SecurityRequirement(name = "jwtAuth")
    @GetMapping("/members/me")
    public MemberDetailsResponse showDetailsOf(Member member) {
        return MemberDetailsResponse.of(member);
    }

    @SecurityRequirement(name = "jwtAuth")
    @PutMapping("/members/me/group")
    public void changeGroup(Member member, @RequestBody GroupRequest groupRequest) {
        memberService.changeGroupNameOf(member, groupRequest.getGroup());
    }

    @SecurityRequirement(name = "jwtAuth")
    @PutMapping("/members/me/name")
    public void changeName(Member member, @RequestBody NameRequest nameRequest) {
        memberService.changeNameOf(member, nameRequest.getName());
    }
}
