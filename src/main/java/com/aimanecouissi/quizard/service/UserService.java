package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.dto.UserDto;
import com.aimanecouissi.quizard.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User findByEmail(String email);

    User findByPasswordResetToken(String token);

    String findEmailByAuthentication(Authentication authentication);

    List<User> findAll();

    boolean existsByEmail(String email);

    void saveAll(List<User> users);

    void create(UserDto userDto);

    void updateName(String email, String firstName, String lastName);

    void updatePasswordResetToken(String email, String token);

    void updatePassword(User user, String newPassword);

    void updatePassword(String email, String newPassword);
}