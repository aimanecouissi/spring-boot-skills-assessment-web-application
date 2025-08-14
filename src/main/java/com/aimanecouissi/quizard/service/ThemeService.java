package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.dto.ThemeDto;
import com.aimanecouissi.quizard.entity.Theme;

import java.util.List;

public interface ThemeService {
    Theme findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);

    List<Theme> findAll();

    List<Theme> findAllByOrderByIdDesc();

    void create(ThemeDto themeDto);
}