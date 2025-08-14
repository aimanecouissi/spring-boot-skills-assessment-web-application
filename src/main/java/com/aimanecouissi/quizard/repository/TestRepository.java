package com.aimanecouissi.quizard.repository;

import com.aimanecouissi.quizard.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
    Page<Test> findAllByOrderByIdDesc(Pageable pageable);

    Page<Test> findAllByNameContainingOrderByIdDesc(String name, Pageable pageable);

    Page<Test> findAllByThemeIdOrderByIdDesc(Long themeId, Pageable pageable);
}