package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();

    void saveAll(List<Role> userRole);
}