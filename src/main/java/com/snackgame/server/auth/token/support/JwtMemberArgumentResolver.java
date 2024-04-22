package com.snackgame.server.auth.token.support;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.snackgame.server.auth.exception.TokenAuthenticationException;
import com.snackgame.server.auth.exception.TokenUnresolvableException;
import com.snackgame.server.auth.token.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtProvider accessTokenProvider;
    private final MemberResolver<?> memberResolver;

    private final Logger log = LoggerFactory.getLogger(getClass());

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

        Cookie foundCookie = getCookieFromRequest(webRequest);
        String cookieToken = foundCookie.getValue();

        return resolveMember(cookieToken);
    }

    private Cookie getCookieFromRequest(NativeWebRequest webRequest) {
        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
        if (request.getCookies() == null) {
            throw new TokenUnresolvableException();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("accessToken"))
                .findFirst()
                .orElseThrow(() -> new TokenUnresolvableException());
    }

    private Object resolveMember(String token) {
        accessTokenProvider.validate(token);

        Long memberId = Long.parseLong(accessTokenProvider.getSubjectFrom(token));
        return memberResolver.resolve(memberId)
                .orElseThrow(TokenAuthenticationException::new);
    }
}
