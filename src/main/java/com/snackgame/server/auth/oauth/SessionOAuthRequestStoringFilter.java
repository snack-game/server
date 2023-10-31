package com.snackgame.server.auth.oauth;

import static org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.OncePerRequestFilter;

public class SessionOAuthRequestStoringFilter extends OncePerRequestFilter {

    public static final String REFERER_ATTRIBUTE_NAME = "OAUTH_REQUEST_REFERER";

    private static final int SESSION_TIMEOUT_SECONDS = 120;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        save(request);
        doFilter(request, response, filterChain);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return isNotOAuthAuthorizationUri(request.getRequestURI());
    }

    private boolean isNotOAuthAuthorizationUri(String requestUri) {
        return !requestUri.startsWith(DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
    }

    private void save(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(SESSION_TIMEOUT_SECONDS);
        session.setAttribute(REFERER_ATTRIBUTE_NAME, request.getHeader("Referer"));
    }
}
