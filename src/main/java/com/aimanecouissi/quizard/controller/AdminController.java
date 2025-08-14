package com.aimanecouissi.quizard.controller;

import com.aimanecouissi.quizard.dto.*;
import com.aimanecouissi.quizard.entity.Question;
import com.aimanecouissi.quizard.entity.Test;
import com.aimanecouissi.quizard.entity.Theme;
import com.aimanecouissi.quizard.entity.User;
import com.aimanecouissi.quizard.enums.Difficulty;
import com.aimanecouissi.quizard.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final ThemeService themeService;
    private final TestService testService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ResultService resultService;

    public AdminController(UserService userService, ThemeService themeService, TestService testService, QuestionService questionService, AnswerService answerService, ResultService resultService) {
        this.userService = userService;
        this.themeService = themeService;
        this.testService = testService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.resultService = resultService;
    }

    @GetMapping("/results")
    public String results(Model model) {
        model.addAttribute("results", resultService.findAll());
        return "admin/results";
    }

    @GetMapping("/settings")
    public String Settings(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("nameDto", new NameDto(user.getFirstName(), user.getLastName()));
        model.addAttribute("passwordDto", new PasswordDto());
        return "admin/settings";
    }

    @PostMapping("/settings/name")
    public String updateName(@AuthenticationPrincipal User user, @Valid @ModelAttribute("nameDto") NameDto nameDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addMessage(redirectAttributes, "error", "Account Settings", "Please enter a valid name.");
        } else {
            userService.updateName(user.getEmail(), nameDto.getFirstName(), nameDto.getLastName());
            user.setFirstName(nameDto.getFirstName());
            user.setLastName(nameDto.getLastName());
            addMessage(redirectAttributes, "success", "Account Settings", "Account updated successfully.");
        }
        return "redirect:/admin/settings";
    }

    @PostMapping("/settings/password")
    public String updatePassword(@AuthenticationPrincipal User user, @Valid @ModelAttribute("passwordDto") PasswordDto passwordDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addMessage(redirectAttributes, "error", "Account Settings", "Please enter a valid password.");
        } else {
            userService.updatePassword(user.getEmail(), passwordDto.getNewPassword());
            addMessage(redirectAttributes, "success", "Account Settings", "Account updated successfully.");
        }
        return "redirect:/admin/settings";
    }

    @GetMapping("/theme")
    public String listThemes(Model model) {
        model.addAttribute("themes", themeService.findAllByOrderByIdDesc());
        return "admin/theme/list";
    }

    @GetMapping("/theme/add")
    public String addThemeGet(Model model) {
        model.addAttribute("themeDto", new ThemeDto());
        return "admin/theme/add";
    }

    @PostMapping("/theme/add")
    public String addThemePost(@Valid @ModelAttribute("themeDto") ThemeDto themeDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addMessage(model, "Theme Management", "Please enter a valid data.");
            return "admin/theme/add";
        } else {
            themeService.create(themeDto);
            addMessage(redirectAttributes, "success", "Theme Management", "Theme added successfully.");
            return "redirect:/admin/theme";
        }
    }

    @GetMapping("/theme/edit/{id}")
    public String editThemeGet(@PathVariable("id") Long id, Model model) {
        if (id == null || !themeService.existsById(id)) {
            return "redirect:/admin/theme";
        } else {
            Theme theme = themeService.findById(id);
            ThemeDto themeDto = new ThemeDto(theme.getId(), theme.getName(), theme.getIcon(), theme.getDescription());
            model.addAttribute("themeDto", themeDto);
            return "/admin/theme/edit";
        }
    }

    @PostMapping("/theme/edit")
    public String editThemePost(@Valid @ModelAttribute("themeDto") ThemeDto themeDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addMessage(model, "Theme Management", "Please enter a valid data.");
            return "admin/theme/edit";
        } else {
            Long themeId = themeDto.getId();
            if (themeId != null && themeService.existsById(themeId)) {
                themeService.create(themeDto);
                addMessage(redirectAttributes, "success", "Theme Management", "Theme updated successfully.");
            }
            return "redirect:/admin/theme";
        }
    }

    @GetMapping("/theme/delete/{id}")
    public String deleteTheme(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (id != null && themeService.existsById(id)) {
            themeService.deleteById(id);
            addMessage(redirectAttributes, "success", "Theme Management", "Theme deleted successfully.");
        }
        return "redirect:/admin/theme";
    }

    @GetMapping("/test")
    public String listTests(@RequestParam(name = "page", defaultValue = "1", required = false) int page, @RequestParam(name = "search", defaultValue = "", required = false) String search, Model model) {
        int pages = testService.getPage(search, null, page, 11).getTotalPages();
        model.addAttribute("tests", testService.getAll(search, null, page, 11));
        model.addAttribute("pages", pages);
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        return "admin/test/list";
    }

    @GetMapping("/test/add")
    public String addTestGet(Model model) {
        model.addAttribute("testDto", new TestDto());
        populateSelectInputs(model);
        return "admin/test/add";
    }

    @PostMapping("/test/add")
    public String addTestPost(@RequestParam("image") MultipartFile multipartFile, @Valid @ModelAttribute("testDto") TestDto testDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors() || multipartFile.isEmpty()) {
            addMessage(model, "Test Management", "Please enter a valid data.");
            populateSelectInputs(model);
            return "admin/test/add";
        } else {
            testService.create(multipartFile, testDto);
            addMessage(redirectAttributes, "success", "Test Management", "Test added successfully.");
            return "redirect:/admin/test";
        }
    }

    @GetMapping("/test/{id}")
    public String testDetails(@PathVariable("id") Long id, Model model) {
        if (id == null || !testService.existsById(id)) {
            return "redirect:/admin/test";
        } else {
            model.addAttribute("test", testService.findById(id));
            model.addAttribute("questions", questionService.findAllByTestId(id));
            return "admin/test/details";
        }
    }

    @GetMapping("/test/edit/{id}")
    public String editTestGet(@PathVariable("id") Long id, Model model) {
        if (id == null || !testService.existsById(id)) {
            return "redirect:/admin/test";
        } else {
            Test test = testService.findById(id);
            TestDto testDto = new TestDto(test.getId(), test.getName(), test.getDescription(), test.getDifficulty(), test.getTheme().getId());
            model.addAttribute("testDto", testDto);
            populateSelectInputs(model);
            return "admin/test/edit";
        }
    }

    @PostMapping("/test/edit")
    public String editTestPost(@RequestParam("image") MultipartFile multipartFile, @Valid @ModelAttribute("testDto") TestDto testDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()) {
            addMessage(model, "Test Management", "Please enter valid data.");
            return "admin/test/edit";
        } else {
            Long testId = testDto.getId();
            if (testId != null && testService.existsById(testId)) {
                testService.update(multipartFile, testDto);
                addMessage(redirectAttributes, "success", "Test Management", "Test updated successfully.");
                return "redirect:/admin/test/" + testId;
            } else {
                return "redirect:/admin/test";
            }
        }
    }

    @GetMapping("/test/delete/{id}")
    public String deleteTest(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) throws IOException {
        if (id != null && testService.existsById(id)) {
            testService.delete(id);
            addMessage(redirectAttributes, "success", "Theme Management", "Test deleted successfully.");
        }
        return "redirect:/admin/test";
    }

    @GetMapping("/question/add/{id}")
    public String addQuestionGet(@PathVariable("id") Long id, Model model) {
        if (id == null || !testService.existsById(id)) {
            return "redirect:/admin/test";
        } else {
            QuestionDto questionDto = new QuestionDto();
            questionDto.setTestId(id);
            model.addAttribute("questionDto", questionDto);
            return "admin/question/add";
        }
    }

    @PostMapping("/question/add")
    public String addQuestionPost(@Valid @ModelAttribute("questionDto") QuestionDto questionDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addMessage(model, "Question Management", "Please enter valid data.");
            return "admin/question/add";
        } else {
            Long testId = questionDto.getTestId();
            if (testId != null && testService.existsById(testId)) {
                questionService.create(questionDto);
                addMessage(redirectAttributes, "success", "Question Management", "Question added successfully.");
                return "redirect:/admin/test/" + testId;
            } else {
                return "redirect:/admin/test";
            }
        }
    }

    @GetMapping("/question/edit/{id}")
    public String editQuestionGet(@PathVariable("id") Long id, Model model) {
        if (id == null || !questionService.existsById(id)) {
            return "redirect:/admin/test";
        } else {
            Question question = questionService.findById(id);
            List<String> options = answerService.findAllNamesByQuestionId(id);
            int answer = answerService.findByQuestionIdAndCorrect(id, true).getInsertionOrder();
            QuestionDto questionDto = new QuestionDto(id, question.getName(), options, answer, question.getTest().getId());
            model.addAttribute("questionDto", questionDto);
            return "admin/question/edit";
        }
    }

    @PostMapping("/question/edit")
    public String editQuestionPost(@Valid @ModelAttribute("questionDto") QuestionDto questionDto, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addMessage(model, "Question Management", "Please enter valid data.");
            return "admin/question/edit";
        } else {
            Long questionId = questionDto.getId();
            if (questionId != null && questionService.existsById(questionId)) {
                Long testId = questionService.findById(questionId).getTest().getId();
                questionService.update(questionDto);
                addMessage(redirectAttributes, "success", "Question Management", "Question updated successfully.");
                return "redirect:/admin/test/" + testId;
            } else {
                return "redirect:/admin/test";
            }
        }
    }

    @GetMapping("/question/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (id != null && questionService.existsById(id)) {
            Long testId = questionService.findById(id).getTest().getId();
            questionService.deleteById(id);
            addMessage(redirectAttributes, "success", "Question Management", "Question deleted successfully.");
            return "redirect:/admin/test/" + testId;
        } else {
            return "redirect:/admin/test";
        }
    }

    private void addMessage(RedirectAttributes redirectAttributes, String type, String title, String message) {
        redirectAttributes.addFlashAttribute("messageType", type);
        redirectAttributes.addFlashAttribute("messageTitle", title);
        redirectAttributes.addFlashAttribute("messageContent", message);
    }

    private void addMessage(Model model, String title, String message) {
        model.addAttribute("messageType", "error");
        model.addAttribute("messageTitle", title);
        model.addAttribute("messageContent", message);
    }

    private void populateSelectInputs(Model model) {
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("themes", themeService.findAll());
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        binder.registerCustomEditor(String.class, stringTrimmerEditor);
    }
}