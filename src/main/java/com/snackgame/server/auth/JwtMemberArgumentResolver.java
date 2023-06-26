package com.snackgame.server.auth;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.snackgame.server.member.business.MemberService;
import com.snackgame.server.member.business.domain.Member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final BearerTokenExtractor bearerTokenExtractor;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String authorization = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String token = bearerTokenExtractor.extract(authorization);
        jwtProvider.validate(token);

        String subject = jwtProvider.getSubjectFrom(token);
        long memberId = Long.parseLong(subject);
        return memberService.findBy(memberId);
    }
}
