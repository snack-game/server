package com.snackgame.server.member.controller.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.snackgame.server.member.business.MemberService;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.exception.InvalidTokenException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final BearerAuthorizationExtractor bearerAuthorizationExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest)webRequest;
        String token = bearerAuthorizationExtractor.extract(request);

        if (jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }
        String payload = jwtTokenProvider.getPayload(token);

        long memberId = Long.parseLong(payload);
        return memberService.findBy(memberId);
    }
}
