package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.entity.Answer;
import com.aimanecouissi.quizard.repository.AnswerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public Answer findByQuestionIdAndCorrect(Long questionId, boolean isCorrect) {
        return answerRepository.findByQuestionIdAndIsCorrect(questionId, isCorrect);
    }

    @Override
    public List<String> findAllNamesByQuestionId(Long questionId) {
        return answerRepository.findAllNamesByQuestionId(questionId);
    }
}