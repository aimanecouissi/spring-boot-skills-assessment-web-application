package com.aimanecouissi.quizard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQuestionDto {
    private String name;
    private int number;
    private List<String> options;
}