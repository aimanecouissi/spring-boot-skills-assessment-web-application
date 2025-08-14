package com.aimanecouissi.quizard.repository;

import com.aimanecouissi.quizard.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    boolean existsByTestIdAndUserId(Long testId, Long userId);

    Progress findByTestIdAndUserId(Long testId, Long userId);
}
