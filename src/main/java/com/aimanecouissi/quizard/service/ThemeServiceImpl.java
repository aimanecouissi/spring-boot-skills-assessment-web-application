package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.dto.ThemeDto;
import com.aimanecouissi.quizard.entity.Theme;
import com.aimanecouissi.quizard.repository.ThemeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeServiceImpl implements ThemeService {
    ThemeRepository themeRepository;

    public ThemeServiceImpl(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public Theme findById(Long id) {
        return themeRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return themeRepository.existsById(id);
    }

    @Override
    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Override
    public List<Theme> findAllByOrderByIdDesc() {
        return themeRepository.findAllByOrderByIdDesc();
    }

    @Override
    public void create(ThemeDto themeDto) {
        Theme theme = getOrCreateTheme(themeDto);
        theme.setName(themeDto.getName());
        theme.setIcon(themeDto.getIcon());
        theme.setDescription(themeDto.getDescription());
        themeRepository.save(theme);
    }

    private Theme getOrCreateTheme(ThemeDto themeDto) {
        return (themeDto.getId() != null && themeRepository.existsById(themeDto.getId())) ? findById(themeDto.getId()) : new Theme();
    }
}