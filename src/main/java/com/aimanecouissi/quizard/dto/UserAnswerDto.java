package com.aimanecouissi.quizard.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswerDto {
    @NotNull
    private Long testId;
    private int questionNo;
    private Integer option;
}