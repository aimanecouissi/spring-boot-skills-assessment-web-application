package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.dto.TestDto;
import com.aimanecouissi.quizard.entity.Test;
import com.aimanecouissi.quizard.repository.QuestionRepository;
import com.aimanecouissi.quizard.repository.TestRepository;
import com.aimanecouissi.quizard.repository.ThemeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final ThemeRepository themeRepository;
    private final QuestionRepository questionRepository;

    public TestServiceImpl(TestRepository testRepository, ThemeRepository themeRepository, QuestionRepository questionRepository) {
        this.testRepository = testRepository;
        this.themeRepository = themeRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Test findById(Long id) {
        return testRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {
        return testRepository.existsById(id);
    }

    @Override
    @Transactional
    public void create(MultipartFile multipartFile, TestDto testDto) throws IOException {
        String image = generateUniqueImageName(multipartFile);
        uploadImage(multipartFile, image);
        Test test = buildTestFromDto(testDto);
        test.setImage(image);
        testRepository.save(test);
    }

    @Override
    @Transactional
    public void update(MultipartFile multipartFile, TestDto testDto) throws IOException {
        Test test = buildTestFromDto(testDto);
        if (!multipartFile.isEmpty()) {
            String newImage = generateUniqueImageName(multipartFile);
            String oldImage = test.getImage();
            updateImage(oldImage, newImage, multipartFile);
            test.setImage(newImage);
        }
        testRepository.save(test);
    }

    @Override
    @Transactional
    public void delete(Long id) throws IOException {
        removeImage(findById(id).getImage());
        testRepository.deleteById(id);
    }

    @Override
    public Map<Test, Integer> getAll(String search, String theme, int page, int size) {
        return getPage(search, theme, page, size).getContent().stream().collect(Collectors.toMap(Function.identity(), test -> questionRepository.countByTestId(test.getId()), (a, b) -> a, LinkedHashMap::new));
    }

    @Override
    public Page<Test> getPage(String search, String theme, int page, int size) {
        return (search != null && !search.isEmpty()) ? testRepository.findAllByNameContainingOrderByIdDesc(search, PageRequest.of(page > 0 ? page - 1 : 0, size)) : (theme != null && !theme.isEmpty()) ? testRepository.findAllByThemeIdOrderByIdDesc(Long.parseLong(theme), PageRequest.of(page > 0 ? page - 1 : 0, size)) : testRepository.findAllByOrderByIdDesc(PageRequest.of(page > 0 ? page - 1 : 0, size));
    }

    private Test buildTestFromDto(TestDto testDto) {
        Test test = (testDto.getId() != null) ? findById(testDto.getId()) : new Test();
        test.setName(testDto.getName());
        test.setDifficulty(testDto.getDifficulty());
        test.setTheme(themeRepository.findById(testDto.getThemeId()).orElse(null));
        test.setDescription(testDto.getDescription());
        return test;
    }

    private String generateUniqueImageName(MultipartFile multipartFile) {
        return UUID.randomUUID() + Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf('.'));
    }

    private void uploadImage(MultipartFile multipartFile, String fileName) throws IOException {
        String uploadDirectory = "./upload";
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        InputStream inputStream = multipartFile.getInputStream();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    private void updateImage(String oldImage, String newImage, MultipartFile multipartFile) throws IOException {
        removeImage(oldImage);
        uploadImage(multipartFile, newImage);
    }

    private void removeImage(String fileName) throws IOException {
        String uploadDirectory = "./upload";
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
        Path filePath = uploadPath.resolve(fileName);
        Files.deleteIfExists(filePath);
    }
}