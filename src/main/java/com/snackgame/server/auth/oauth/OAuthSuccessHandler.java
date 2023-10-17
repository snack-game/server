package com.snackgame.server.auth.oauth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        backToService(request, response);
    }

    private void backToService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String redirectHost = getRedirectUrlFrom(session);
        response.sendRedirect(redirectHost);
        session.removeAttribute(SessionOAuthRequestStoringFilter.REFERER_ATTRIBUTE_NAME);
    }

    private String getRedirectUrlFrom(HttpSession session) {
        return getRefererFrom(session) + "oauth/success";
    }

    private String getRefererFrom(HttpSession session) {
        String referer = (String)session.getAttribute(SessionOAuthRequestStoringFilter.REFERER_ATTRIBUTE_NAME);
        if (referer == null) {
            return "https://snackga.me/";
        }
        return referer;
    }
}
