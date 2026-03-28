package com.aimanecouissi.quizard.repository;

import com.aimanecouissi.quizard.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}