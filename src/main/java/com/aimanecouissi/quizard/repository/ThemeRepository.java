package com.aimanecouissi.quizard.repository;

import com.aimanecouissi.quizard.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findAllByOrderByIdDesc();
}
