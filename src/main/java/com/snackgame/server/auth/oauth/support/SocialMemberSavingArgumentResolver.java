package com.snackgame.server.auth.oauth.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.snackgame.server.auth.exception.OAuthAuthenticationException;
import com.snackgame.server.auth.oauth.attributes.OAuthAttributes;
import com.snackgame.server.auth.oauth.attributes.OAuthAttributesResolver;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SocialMemberSavingArgumentResolver implements HandlerMethodArgumentResolver {

    private final SocialMemberSaver<?> socialMemberSaver;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JustAuthenticated.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        OAuth2AuthenticationToken authentication = getLastOAuthAuthentication();
        String registrationId = authentication.getAuthorizedClientRegistrationId();
        invalidate(getSessionFrom(webRequest));

        OAuthAttributes attributes = OAuthAttributesResolver.valueOf(registrationId.toUpperCase())
                .resolve(authentication.getPrincipal().getAttributes());

        return socialMemberSaver.saveMemberFrom(attributes);
    }

    private OAuth2AuthenticationToken getLastOAuthAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            return (OAuth2AuthenticationToken)authentication;
        }
        throw new OAuthAuthenticationException("소셜 로그인을 하지 않았습니다");
    }

    private HttpSession getSessionFrom(NativeWebRequest webRequest) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request != null) {
            return request.getSession(false);
        }
        throw new OAuthAuthenticationException("올바른 요청이 아닙니다");
    }

    private void invalidate(HttpSession session) {
        if (session != null) {
            session.invalidate();
            return;
        }
        throw new OAuthAuthenticationException("세션이 없습니다");
    }
}
