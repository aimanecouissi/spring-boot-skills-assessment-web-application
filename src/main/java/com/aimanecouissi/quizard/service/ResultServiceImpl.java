package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.entity.Result;
import com.aimanecouissi.quizard.entity.User;
import com.aimanecouissi.quizard.repository.ResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;

    public ResultServiceImpl(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @Override
    public boolean existsByTestIdAndUserId(Long testId, Long userId) {
        return resultRepository.existsByTestIdAndUserId(testId, userId);
    }

    @Override
    public Result findByTestIdAndUserId(Long testId, Long userId) {
        return resultRepository.findByTestIdAndUserId(testId, userId);
    }

    @Override
    public Result findById(Long id) {
        return resultRepository.findById(id).orElse(null);
    }

    @Override
    public List<Result> findAll() {
        return resultRepository.findAll();
    }


    @Override
    public List<Result> findAllByUser(User user) {
        return resultRepository.findAllByUser(user);
    }


    @Override
    public Result save(Result result) {
        return resultRepository.save(result);
    }
}
