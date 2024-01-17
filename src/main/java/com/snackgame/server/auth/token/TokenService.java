package com.snackgame.server.auth.token;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.auth.token.domain.RefreshToken;
import com.snackgame.server.auth.token.domain.RefreshTokenRepository;
import com.snackgame.server.auth.token.util.JwtProvider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${security.jwt.token.refresh-expire-length}")
    private long refreshTokenExpiry;

    private final JwtProvider accessTokenProvider;
    private final JwtProvider refreshTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenDto issueFor(Long memberId) {
        return new TokenDto(
                issueAccessTokenFor(memberId),
                issueRefreshTokenFor(memberId),
                Duration.ofSeconds(refreshTokenExpiry)
        );
    }

    @Transactional
    public TokenDto reissueFrom(String refreshToken) {
        refreshTokenProvider.validate(refreshToken);

        return new TokenDto(
                reissueAccessTokenFrom(refreshToken),
                reissueRefreshTokenFrom(refreshToken),
                Duration.ofSeconds(refreshTokenExpiry)
        );
    }

    private String issueAccessTokenFor(Long memberId) {
        return accessTokenProvider.createTokenWith(memberId);
    }

    private String issueRefreshTokenFor(Long memberId) {
        String refreshToken = refreshTokenProvider.createTokenWith(memberId);
        return refreshTokenRepository.save(new RefreshToken(refreshToken))
                .getToken();
    }

    private String reissueAccessTokenFrom(String refreshToken) {
        String subject = refreshTokenProvider.getSubjectFrom(refreshToken);
        return issueAccessTokenFor(Long.parseLong(subject));
    }

    private String reissueRefreshTokenFrom(String refreshToken) {
        String subject = refreshTokenProvider.getSubjectFrom(refreshToken);
        String newRefreshToken = issueRefreshTokenFor(Long.parseLong(subject));
        refreshTokenRepository.deleteByToken(refreshToken);
        return newRefreshToken;
    }

    @RequiredArgsConstructor
    @Getter
    public static class TokenDto {

        private final String accessToken;
        private final String refreshToken;
        private final Duration refreshTokenExpiry;
    }
}
