package com.snackgame.server.auth.token.support;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.util.WebUtils;

import com.snackgame.server.auth.exception.TokenUnresolvableException;
import com.snackgame.server.auth.token.dto.TokensDto;
import com.snackgame.server.auth.token.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokensResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider accessTokenProvider;
    private final JwtProvider refreshTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokensFromCookie.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        validateAnnotationFrom(parameter);
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        return new TokensDto(
                extractCookieFrom(request, accessTokenProvider.getCanonicalName()),
                extractCookieFrom(request, refreshTokenProvider.getCanonicalName())
        );
    }

    private String extractCookieFrom(HttpServletRequest request, String cookieName) {
        Cookie cookie = WebUtils.getCookie(request, cookieName);
        if (cookie != null) {
            return cookie.getValue();
        }
        throw new TokenUnresolvableException();
    }

    private void validateAnnotationFrom(MethodParameter methodParameter) {
        TokensFromCookie annotation = methodParameter.getParameterAnnotation(TokensFromCookie.class);
        if (annotation.refreshTokenValidated()) {
            throw new NotImplementedException("구현되지 않은 기능을 사용했습니다");
        }
    }
}
