package com.aimanecouissi.quizard.repository;

import com.aimanecouissi.quizard.entity.Result;
import com.aimanecouissi.quizard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    boolean existsByTestIdAndUserId(Long testId, Long userId);

    Result findByTestIdAndUserId(Long testId, Long userId);

    List<Result> findAllByUser(User user);
}
