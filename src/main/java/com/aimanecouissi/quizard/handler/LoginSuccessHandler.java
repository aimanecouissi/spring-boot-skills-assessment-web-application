package com.aimanecouissi.quizard.handler;

import com.aimanecouissi.quizard.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();
        String redirectURL;
        if (user.hasRole("ROLE_ADMIN")) {
            redirectURL = "admin/test";
        } else {
            redirectURL = request.getContextPath();
        }
        response.sendRedirect(redirectURL);
    }
}