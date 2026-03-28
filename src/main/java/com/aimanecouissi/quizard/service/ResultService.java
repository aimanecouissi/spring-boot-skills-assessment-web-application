package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.entity.Result;
import com.aimanecouissi.quizard.entity.User;

import java.util.List;

public interface ResultService {
    boolean existsByTestIdAndUserId(Long testId, Long userId);

    Result findByTestIdAndUserId(Long testId, Long userId);

    Result findById(Long id);

    List<Result> findAll();

    List<Result> findAllByUser(User user);

    Result save(Result result);
}
