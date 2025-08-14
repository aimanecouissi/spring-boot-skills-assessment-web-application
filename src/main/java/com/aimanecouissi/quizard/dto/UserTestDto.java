package com.aimanecouissi.quizard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTestDto {
    private Long id;
    private String name;
    private String image;
    private int questionsCount;
    private int progress;
}