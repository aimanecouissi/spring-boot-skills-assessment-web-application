package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.entity.Progress;

public interface ProgressService {
    boolean existsByTestIdAndUserId(Long testId, Long userId);

    Progress findByTestIdAndUserId(Long testId, Long userId);

    Progress save(Progress progress);
}
