package com.snackgame.server.member.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snackgame.server.member.business.MemberService;
import com.snackgame.server.member.controller.auth.JwtTokenProvider;
import com.snackgame.server.member.controller.dto.GroupRequest;
import com.snackgame.server.member.controller.dto.MemberRequest;
import com.snackgame.server.member.controller.dto.NameRequest;
import com.snackgame.server.member.controller.dto.TokenResponse;
import com.snackgame.server.member.business.domain.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/members")
    public TokenResponse addMember(@RequestBody MemberRequest memberRequest) {
        Member added = memberService.createWith(memberRequest.getName(), memberRequest.getGroup());
        String accessToken = jwtTokenProvider.createTokenWith(added.getId().toString());
        return new TokenResponse(accessToken);
    }

    @PostMapping("/members/guest")
    public TokenResponse addGuest() {
        Member guest = memberService.createGuest();
        String accessToken = jwtTokenProvider.createTokenWith(guest.getId().toString());
        return new TokenResponse(accessToken);
    }

    @PutMapping("/members/me/group")
    public void changeGroup(Member member, @RequestBody GroupRequest groupRequest) {
        memberService.changeGroupNameOf(member, groupRequest.getGroup());
    }

    @PutMapping("/members/me/name")
    public void changeName(Member member, @RequestBody NameRequest nameRequest) {
        memberService.changeNameOf(member, nameRequest.getName());
    }
}
