package com.snackgame.server.auth.jwt;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.snackgame.server.auth.exception.TokenAuthenticationException;
import com.snackgame.server.member.business.domain.Member;
import com.snackgame.server.member.business.domain.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final BearerTokenExtractor bearerTokenExtractor;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Member.class.isAssignableFrom(parameter.getParameterType())
               && !parameter.hasParameterAnnotations();
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
        jwtProvider.validate(token);

        Long memberId = Long.parseLong(jwtProvider.getSubjectFrom(token));
        return memberRepository.findById(memberId)
                .orElseThrow(TokenAuthenticationException::new);
    }
}
