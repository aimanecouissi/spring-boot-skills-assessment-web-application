package com.aimanecouissi.quizard.repository;

import com.aimanecouissi.quizard.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByTestId(Long testId);

    int countByTestId(Long testId);
}