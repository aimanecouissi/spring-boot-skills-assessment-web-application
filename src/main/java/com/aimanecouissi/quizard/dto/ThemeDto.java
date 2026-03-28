package com.aimanecouissi.quizard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThemeDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String icon;
    @NotBlank
    private String description;
}