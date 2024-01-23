package com.snackgame.server.auth.token;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.snackgame.server.annotation.ServiceTest;
import com.snackgame.server.auth.token.domain.RefreshToken;
import com.snackgame.server.auth.token.domain.RefreshTokenRepository;
import com.snackgame.server.member.MemberService;
import com.snackgame.server.member.domain.Member;

@ServiceTest
class TokenServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void 리프레시_토큰_저장_검증() {
        Member created = memberService.createGuest();

        String refreshToken = tokenService.issueFor(created.getId()).getRefreshToken();

        assertThat(refreshTokenRepository.findAll()).map(RefreshToken::getToken).first().isEqualTo(refreshToken);
    }

    @Test
    void 리프레시_토큰_삭제_검증() {
        Member created = memberService.createGuest();
        String refreshToken = tokenService.issueFor(created.getId()).getRefreshToken();

        tokenService.delete(refreshToken);

        assertThat(refreshTokenRepository.findAll()).isEmpty();
    }

}