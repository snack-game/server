package com.snackgame.server.auth.jwt.support;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.snackgame.server.auth.exception.TokenAuthenticationException;
import com.snackgame.server.auth.jwt.util.BearerTokenExtractor;
import com.snackgame.server.auth.jwt.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final BearerTokenExtractor bearerTokenExtractor = new BearerTokenExtractor();
    private final JwtProvider accessTokenProvider;
    private final MemberResolver<?> memberResolver;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticated.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = bearerTokenExtractor.extract(authorization);
        accessTokenProvider.validate(token);

        Long memberId = Long.parseLong(accessTokenProvider.getSubjectFrom(token));
        return memberResolver.resolve(memberId)
                .orElseThrow(TokenAuthenticationException::new);
    }
}
