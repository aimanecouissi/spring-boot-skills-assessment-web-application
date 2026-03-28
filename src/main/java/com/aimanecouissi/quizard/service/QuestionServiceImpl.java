package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.dto.QuestionDto;
import com.aimanecouissi.quizard.entity.Answer;
import com.aimanecouissi.quizard.entity.Question;
import com.aimanecouissi.quizard.repository.AnswerRepository;
import com.aimanecouissi.quizard.repository.QuestionRepository;
import com.aimanecouissi.quizard.repository.TestRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;
    private final AnswerRepository answerRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, TestRepository testRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.testRepository = testRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public Question findById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Question> findAllByTestId(Long testId) {
        return questionRepository.findAllByTestId(testId);
    }

    @Override
    public boolean existsById(Long id) {
        return questionRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    public int countByTestId(Long testId) {
        return questionRepository.countByTestId(testId);
    }

    @Override
    @Transactional
    public void create(QuestionDto questionDto) {
        Question question = new Question();
        question.setName(questionDto.getName());
        question.setTest(testRepository.findById(questionDto.getTestId()).orElse(null));
        questionRepository.save(question);
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < questionDto.getAnswers().size(); i++) {
            Answer answer = new Answer();
            answer.setName(questionDto.getAnswers().get(i));
            answer.setInsertionOrder(i + 1);
            answer.setCorrect(i == questionDto.getAnswer() - 1);
            answer.setQuestion(question);
            answers.add(answer);
        }
        answerRepository.saveAll(answers);
    }

    @Override
    @Transactional
    public void update(QuestionDto questionDto) {
        Question question = findById(questionDto.getId());
        question.setName(questionDto.getName());
        List<Answer> answers = answerRepository.findAllByQuestionId(questionDto.getId());
        for (int i = 0; i < answers.size(); i++) {
            answers.get(i).setName(questionDto.getAnswers().get(i));
            answers.get(i).setCorrect(questionDto.getAnswer() - 1 == i);
        }
        questionRepository.save(question);
        answerRepository.saveAll(answers);
    }
}