package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.dto.TestDto;
import com.aimanecouissi.quizard.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface TestService {
    Test findById(Long id);

    boolean existsById(Long id);

    void create(MultipartFile multipartFile, TestDto testDTO) throws IOException;

    void update(MultipartFile multipartFile, TestDto testDTO) throws IOException;

    void delete(Long id) throws IOException;

    Map<Test, Integer> getAll(String search, String theme, int page, int size);

    Page<Test> getPage(String search, String theme, int page, int size);
}