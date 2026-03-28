package com.aimanecouissi.quizard.handler;

import com.aimanecouissi.quizard.oauth2.ApplicationOAuth2User;
import com.aimanecouissi.quizard.service.UserAuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UserAuthenticationService userAuthenticationService;

    public OAuth2LoginSuccessHandler(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        ApplicationOAuth2User applicationOAuth2User = (ApplicationOAuth2User) authentication.getPrincipal();
        userAuthenticationService.oAuth2Signup(applicationOAuth2User.getEmail(), applicationOAuth2User.getFullName(), applicationOAuth2User.getOauth2ClientName());
        super.onAuthenticationSuccess(request, response, authentication);
    }
}