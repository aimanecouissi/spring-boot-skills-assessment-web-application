package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.entity.Answer;

import java.util.List;

public interface AnswerService {
    Answer findByQuestionIdAndCorrect(Long questionId, boolean isCorrect);

    List<String> findAllNamesByQuestionId(Long questionId);
}