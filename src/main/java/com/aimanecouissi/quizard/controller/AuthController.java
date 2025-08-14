package com.aimanecouissi.quizard.controller;

import com.aimanecouissi.quizard.dto.ForgotDto;
import com.aimanecouissi.quizard.dto.ResetDto;
import com.aimanecouissi.quizard.dto.UserDto;
import com.aimanecouissi.quizard.entity.User;
import com.aimanecouissi.quizard.service.EmailSenderService;
import com.aimanecouissi.quizard.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Controller
public class AuthController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    public AuthController(UserService userService, EmailSenderService emailSenderService) {
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/signin")
    public String signin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "auth/signin";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/signup")
    public String signupGet(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupPost(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        } else {
            if (userService.existsByEmail(userDto.getEmail())) {
                addMessage(model);
                return "auth/signup";
            } else {
                userService.create(userDto);
                addMessage(redirectAttributes);
                return "redirect:/signin";
            }
        }
    }

    @GetMapping("/password/forgot")
    public String forgotPasswordGet(Model model) {
        model.addAttribute("forgotDto", new ForgotDto());
        return "auth/forgot";
    }

    @PostMapping("/password/forgot")
    public String forgotPasswordPost(@Valid @ModelAttribute("forgotDto") ForgotDto forgotDto, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            return "auth/forgot";
        } else {
            String email = forgotDto.getEmail();
            if (userService.existsByEmail(email)) {
                String token = UUID.randomUUID().toString();
                userService.updatePasswordResetToken(email, token);
                String passwordResetLink = "http://localhost:8080/password/reset?token=" + token;
                emailSenderService.sendPasswordResetLink(email, passwordResetLink);
            }
            return "redirect:/password/forgot?success";
        }
    }

    @GetMapping("/password/reset")
    public String resetPasswordGet(@Param(value = "token") String token, Model model) {
        if (token == null || userService.findByPasswordResetToken(token) == null) {
            model.addAttribute("error", "Please use the link sent to your email address.");
        }
        model.addAttribute("resetDto", new ResetDto(token, null));
        return "auth/reset";
    }

    @PostMapping("/password/reset")
    public String resetPasswordPost(@Valid @ModelAttribute("resetDto") ResetDto resetDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            String token = resetDto.getToken();
            String newPassword = resetDto.getNewPassword();
            User user = userService.findByPasswordResetToken(token);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Please use the link sent to your email address.");
            } else {
                userService.updatePassword(user, newPassword);
                redirectAttributes.addFlashAttribute("success", "Password reset successfully.");
            }
        }
        return "redirect:/password/reset";
    }

    private void addMessage(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("messageTitle", "Account Creation");
        redirectAttributes.addFlashAttribute("messageContent", "Account created successfully.");
    }

    private void addMessage(Model model) {
        model.addAttribute("messageType", "error");
        model.addAttribute("messageTitle", "Account Creation");
        model.addAttribute("messageContent", "Email address already used.");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
}