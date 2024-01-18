package com.snackgame.server.auth.token;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.auth.exception.TokenExpiredException;
import com.snackgame.server.auth.token.domain.RefreshToken;
import com.snackgame.server.auth.token.domain.RefreshTokenRepository;
import com.snackgame.server.auth.token.dto.TokenDto;
import com.snackgame.server.auth.token.util.JwtProvider;

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
        handleExpiration(() -> refreshTokenProvider.validate(refreshToken));
        return new TokenDto(
                reissueAccessTokenFrom(refreshToken),
                reissueRefreshTokenFrom(refreshToken),
                Duration.ofSeconds(refreshTokenExpiry)
        );
    }

    private void handleExpiration(Runnable runnable) {
        try {
            runnable.run();
        } catch (TokenExpiredException exception) {
            throw exception.withLogoutAction();
        }
    }

    private String issueAccessTokenFor(Long memberId) {
        return accessTokenProvider.createTokenWith(memberId.toString());
    }

    private String issueRefreshTokenFor(Long memberId) {
        String refreshToken = refreshTokenProvider.createTokenWith(memberId.toString());
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
}
