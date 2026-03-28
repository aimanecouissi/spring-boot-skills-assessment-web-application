package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.entity.Progress;
import com.aimanecouissi.quizard.repository.ProgressRepository;
import org.springframework.stereotype.Service;

@Service
public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepository;

    public ProgressServiceImpl(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @Override
    public boolean existsByTestIdAndUserId(Long testId, Long userId) {
        return progressRepository.existsByTestIdAndUserId(testId, userId);
    }

    @Override
    public Progress findByTestIdAndUserId(Long testId, Long userId) {
        return progressRepository.findByTestIdAndUserId(testId, userId);
    }

    @Override
    public Progress save(Progress progress) {
        return progressRepository.save(progress);
    }
}
