package com.aimanecouissi.quizard.dto;

import com.aimanecouissi.quizard.enums.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Difficulty difficulty;
    @NotNull
    private Long themeId;
}