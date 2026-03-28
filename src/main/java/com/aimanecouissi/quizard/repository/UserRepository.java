package com.aimanecouissi.quizard.repository;

import com.aimanecouissi.quizard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByPasswordResetToken(String token);

    boolean existsByEmail(String email);
}