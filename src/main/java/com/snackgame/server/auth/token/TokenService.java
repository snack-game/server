package com.snackgame.server.auth.token;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snackgame.server.auth.exception.RefreshTokenExpiredException;
import com.snackgame.server.auth.exception.TokenExpiredException;
import com.snackgame.server.auth.token.domain.RefreshToken;
import com.snackgame.server.auth.token.domain.RefreshTokenRepository;
import com.snackgame.server.auth.token.dto.TokensDto;
import com.snackgame.server.auth.token.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider accessTokenProvider;
    private final JwtProvider refreshTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokensDto issueFor(Long memberId) {
        return new TokensDto(
                issueAccessTokenFor(memberId),
                issueRefreshTokenFor(memberId)
        );
    }

    @Transactional
    public TokensDto reissueFrom(String refreshToken) {
        handleTokenExpiry(() -> refreshTokenProvider.validate(refreshToken));
        return new TokensDto(
                reissueAccessTokenFrom(refreshToken),
                reissueRefreshTokenFrom(refreshToken)
        );
    }

    @Transactional
    public void delete(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    private void handleTokenExpiry(Runnable runnable) {
        try {
            runnable.run();
        } catch (TokenExpiredException exception) {
            throw new RefreshTokenExpiredException();
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
