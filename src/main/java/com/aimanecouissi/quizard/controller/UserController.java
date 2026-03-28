package com.aimanecouissi.quizard.controller;

import com.aimanecouissi.quizard.dto.*;
import com.aimanecouissi.quizard.entity.*;
import com.aimanecouissi.quizard.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final TestService testService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ProgressService progressService;
    private final ResultService resultService;
    private final PdfService pdfService;
    private final int MAX_ATTEMPTS = 2;

    public UserController(UserService userService, TestService testService, QuestionService questionService, AnswerService answerService, ProgressService progressService, ResultService resultService, PdfService pdfService) {
        this.userService = userService;
        this.testService = testService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.progressService = progressService;
        this.resultService = resultService;
        this.pdfService = pdfService;
    }

    @GetMapping("/settings")
    public String Settings(Model model) {
        model.addAttribute("nameDto", new NameDto(getUser().getFirstName(), getUser().getLastName()));
        model.addAttribute("passwordDto", new PasswordDto());
        return "user/settings";
    }

    @PostMapping("/settings/name")
    public String updateName(@AuthenticationPrincipal User user, RedirectAttributes redirectAttributes, @Valid @ModelAttribute("nameDto") NameDto nameDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            addMessage(redirectAttributes, "error", "Account Settings", "Please enter a valid name.");
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userService.updateName(getUser().getEmail(), nameDto.getFirstName(), nameDto.getLastName());
            if (authentication.getPrincipal() instanceof User) {
                user.setFirstName(nameDto.getFirstName());
                user.setLastName(nameDto.getLastName());
            }
            addMessage(redirectAttributes, "success", "Account Settings", "Account updated successfully.");
        }
        return "redirect:/user/settings";
    }

    @PostMapping("/settings/password")
    public String updatePassword(@Valid @ModelAttribute("passwordDto") PasswordDto passwordDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addMessage(redirectAttributes, "error", "Account Settings", "Please enter a valid password.");
        } else {
            userService.updatePassword(getUser().getEmail(), passwordDto.getNewPassword());
            addMessage(redirectAttributes, "success", "Account Settings", "Account updated successfully.");
        }
        return "redirect:/user/settings";
    }

    @GetMapping("/test/{id}")
    public String testGet(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        if (id != null && testService.existsById(id)) {
            Test test = testService.findById(id);
            Progress progress = getOrCreateProgress(id);
            if (progress.getAttempts() == MAX_ATTEMPTS) {
                addMessage(redirectAttributes, "info", "Quizard", "You have reached the max attempts for this test.");
                return "redirect:/";
            } else {
                int currentQuestion = getCurrentQuestion(progress, id);
                if (currentQuestion == 0) {
                    return handleTestCompletion(id, progress);
                } else {
                    Question question = getQuestionByNumber(id, currentQuestion);
                    populateModelAttributes(model, id, test, currentQuestion, question);
                    return "user/test";
                }
            }
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/test")
    public String testPost(@Valid @ModelAttribute("userAnswerDto") UserAnswerDto userAnswerDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addMessage(redirectAttributes, "error", "Quizard", "Please choose a valid option next time.");
        } else {
            Test test = testService.findById(userAnswerDto.getTestId());
            if (test != null) {
                Progress progress = progressService.findByTestIdAndUserId(test.getId(), getUser().getId());
                if (progress.getAttempts() == MAX_ATTEMPTS) {
                    addMessage(redirectAttributes, "info", "Quizard", "You have reached the max attempts for this test.");
                    return "redirect:/";
                } else {
                    if (progress.getCurrentQuestion() != userAnswerDto.getQuestionNo()) {
                        addMessage(redirectAttributes, "error", "Quizard", "Please choose a valid option next time.");
                    } else {
                        processUserAnswer(userAnswerDto, progress, test);
                    }
                    return "redirect:/user/test/" + userAnswerDto.getTestId();
                }
            }
        }
        return "redirect:/";
    }

    @GetMapping("/result/{id}")
    public String result(@PathVariable(name = "id") Long id, Model model) {
        Result result = resultService.findById(id);
        if (result == null || !getUser().getId().equals(result.getUser().getId())) {
            return "home/access-denied";
        } else {
            Test test = testService.findById(result.getTest().getId());
            int questionsCount = questionService.countByTestId(test.getId());
            int answersCount = result.getScore() * questionsCount / 100;
            model.addAttribute("userResultDto", new UserResultDto(id, test, questionsCount, answersCount, result.getScore()));
            return "user/result";
        }
    }

    @GetMapping("/results")
    public String results(Model model) {
        model.addAttribute("results", resultService.findAllByUser(getUser()));
        return "user/results";
    }

    @GetMapping("/certificate/{id}")
    public void certificate(@PathVariable(name = "id") Long id, HttpServletResponse httpServletResponse) throws Exception {
        Result result = resultService.findById(id);
        if (id != null && result != null && Objects.equals(result.getUser().getId(), getUser().getId()) && result.getScore() >= 80) {
            Path file = Paths.get(pdfService.generatePdf(id).getAbsolutePath());
            if (Files.exists(file)) {
                httpServletResponse.setContentType("application/pdf");
                httpServletResponse.addHeader("Content-Disposition", "attachment; filename" + file.getFileName());
                Files.copy(file, httpServletResponse.getOutputStream());
                httpServletResponse.getOutputStream().flush();
            }
        }
    }

    private String handleTestCompletion(Long testId, Progress progress) {
        Result result = getOrCreateResult(testId, progress);
        resetProgress(progress);
        Long resultId = resultService.save(result).getId();
        return "redirect:/user/result/" + resultId;
    }

    private Result getOrCreateResult(Long testId, Progress progress) {
        if (!resultService.existsByTestIdAndUserId(testId, getUser().getId())) {
            return new Result(null, getUser(), testService.findById(testId), progress.getScore(), LocalDate.now());
        } else {
            Result result = resultService.findByTestIdAndUserId(testId, getUser().getId());
            if (result.getScore() < progress.getScore()) {
                result.setScore(progress.getScore());
                result.setDate(LocalDate.now());
            }
            return result;
        }
    }

    private void resetProgress(Progress progress) {
        progress.setCurrentQuestion(0);
        progress.setScore(0);
        progress.setAttempts(progress.getAttempts() + 1);
        progressService.save(progress);
    }

    private int getCurrentQuestion(Progress progress, Long testId) {
        int currentQuestion = progress.getCurrentQuestion() + 1;
        if (questionService.countByTestId(testId) >= currentQuestion) {
            progress.setCurrentQuestion(currentQuestion);
            progressService.save(progress);
            return currentQuestion;
        } else {
            return 0;
        }
    }

    private Progress getOrCreateProgress(Long testId) {
        if (!progressService.existsByTestIdAndUserId(testId, getUser().getId())) {
            int DEFAULT_CURRENT_QUESTION = 0;
            int DEFAULT_ATTEMPTS = 0;
            int DEFAULT_SCORE = 0;
            return progressService.save(new Progress(null, getUser(), testService.findById(testId), DEFAULT_CURRENT_QUESTION, DEFAULT_ATTEMPTS, DEFAULT_SCORE));
        } else {
            return progressService.findByTestIdAndUserId(testId, getUser().getId());
        }
    }

    private Question getQuestionByNumber(Long testId, int currentQuestion) {
        return questionService.findAllByTestId(testId).get(currentQuestion - 1);
    }

    private void populateModelAttributes(Model model, Long id, Test test, int currentQuestion, Question question) {
        model.addAttribute("userTestDto", new UserTestDto(test.getId(), test.getName(), test.getImagePath(), questionService.countByTestId(id), currentQuestion * 100 / questionService.countByTestId(id)));
        model.addAttribute("userQuestionDto", new UserQuestionDto(question.getName(), currentQuestion, answerService.findAllNamesByQuestionId(question.getId())));
        UserAnswerDto userAnswerDto = new UserAnswerDto();
        userAnswerDto.setTestId(id);
        userAnswerDto.setQuestionNo(currentQuestion);
        model.addAttribute("userAnswerDto", userAnswerDto);
    }

    private void processUserAnswer(UserAnswerDto userAnswerDto, Progress progress, Test test) {
        List<Question> questions = questionService.findAllByTestId(test.getId());
        Question question = questions.get(userAnswerDto.getQuestionNo() - 1);
        int correctAnswer = answerService.findByQuestionIdAndCorrect(question.getId(), true).getInsertionOrder();
        if (userAnswerDto.getOption() != null && userAnswerDto.getOption() == correctAnswer) {
            int questionsCount = questionService.countByTestId(test.getId());
            progress.setScore(progress.getScore() + (100 / questionsCount));
            progressService.save(progress);
        }
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByEmail(userService.findEmailByAuthentication(authentication));
    }

    private void addMessage(RedirectAttributes redirectAttributes, String type, String title, String message) {
        redirectAttributes.addFlashAttribute("messageType", type);
        redirectAttributes.addFlashAttribute("messageTitle", title);
        redirectAttributes.addFlashAttribute("messageContent", message);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
}