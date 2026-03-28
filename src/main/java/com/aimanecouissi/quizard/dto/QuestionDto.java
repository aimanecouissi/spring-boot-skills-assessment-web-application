package com.aimanecouissi.quizard.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
    private Long id;
    @NotBlank
    private String name;
    @NotEmpty
    @Size(min = 4, max = 4)
    private List<String> answers;
    @Min(1)
    @Max(4)
    private int answer;
    private Long testId;
}