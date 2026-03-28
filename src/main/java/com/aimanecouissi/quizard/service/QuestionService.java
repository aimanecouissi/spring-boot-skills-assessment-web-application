package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.dto.QuestionDto;
import com.aimanecouissi.quizard.entity.Question;

import java.util.List;

public interface QuestionService {
    Question findById(Long id);

    List<Question> findAllByTestId(Long testId);

    void deleteById(Long id);

    boolean existsById(Long id);

    int countByTestId(Long testId);

    void create(QuestionDto questionDto);

    void update(QuestionDto questionDto);
}