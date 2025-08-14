package com.aimanecouissi.quizard.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender javaMailSender;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendPasswordResetLink(String email, String passwordResetLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage);
        mimeMessageHelper.setFrom("no-reply@quizard.com", "Quizard");
        mimeMessageHelper.setTo(email);
        String subject = "Password Reset Link";
        String message = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>" + "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + passwordResetLink + "\">Change my password</a></p>";
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message, true);
        javaMailSender.send(mimeMailMessage);
        mimeMessageHelper.setSubject("Password Reset Link");
    }
}