package com.aimanecouissi.quizard.service;

import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailSenderService {
    void sendPasswordResetLink(String email, String passwordResetLink) throws MessagingException, UnsupportedEncodingException;
}