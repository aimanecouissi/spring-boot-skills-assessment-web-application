package com.aimanecouissi.quizard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetDto {
    @NotBlank
    private String token;
    @NotBlank
    private String newPassword;
}