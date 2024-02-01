package com.snackgame.server.auth.token.support;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.snackgame.server.auth.exception.TokenAuthenticationException;
import com.snackgame.server.auth.token.util.BearerTokenExtractor;
import com.snackgame.server.auth.token.util.JwtProvider;

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
        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();

        Long memberId = null ;

        if(request.getCookies() != null) {
            Optional<Cookie> foundCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("accessToken"))
                    .findFirst();
            if(foundCookie.isPresent()){
                String cookieToken = foundCookie.get().getValue();
                accessTokenProvider.validate(cookieToken);

                memberId = Long.parseLong(accessTokenProvider.getSubjectFrom(cookieToken));
            }

        } else {
            String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
            String headerToken = bearerTokenExtractor.extract(authorization);
            accessTokenProvider.validate(headerToken);

            memberId = Long.parseLong(accessTokenProvider.getSubjectFrom(headerToken));
        }
        return memberResolver.resolve(memberId)
                .orElseThrow(TokenAuthenticationException::new);
    }
}
