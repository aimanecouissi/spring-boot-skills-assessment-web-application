package com.aimanecouissi.quizard.repository;

import com.aimanecouissi.quizard.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionId(Long questionId);

    Answer findByQuestionIdAndIsCorrect(Long questionId, boolean isCorrect);

    @Query("SELECT a.name FROM Answer a WHERE a.question.id = :questionId")
    List<String> findAllNamesByQuestionId(Long questionId);
}