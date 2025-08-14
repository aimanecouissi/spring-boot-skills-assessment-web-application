package com.aimanecouissi.quizard.dto;

import com.aimanecouissi.quizard.entity.Test;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResultDto {
    private Long id;
    private Test test;
    private int questionsCount;
    private int answersCount;
    private int score;
}