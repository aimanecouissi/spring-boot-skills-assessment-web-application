package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.enums.Provider;

public interface UserAuthenticationService {
    void oAuth2Signup(String email, String name, Provider provider);
}