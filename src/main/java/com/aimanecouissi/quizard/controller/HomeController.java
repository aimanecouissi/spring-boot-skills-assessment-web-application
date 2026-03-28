package com.aimanecouissi.quizard.controller;

import com.aimanecouissi.quizard.service.TestService;
import com.aimanecouissi.quizard.service.ThemeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final ThemeService themeService;
    private final TestService testService;

    public HomeController(ThemeService themeService, TestService testService) {
        this.themeService = themeService;
        this.testService = testService;
    }

    @GetMapping("/")
    public String index(@RequestParam(name = "page", defaultValue = "1", required = false) int page, @RequestParam(name = "search", required = false) String search, @RequestParam(name = "theme", required = false) String theme, Model model) {
        int pages = testService.getPage(search, theme, page, 12).getTotalPages();
        model.addAttribute("tests", testService.getAll(search, theme, page, 12));
        model.addAttribute("pages", pages);
        model.addAttribute("themes", themeService.findAll());
        model.addAttribute("search", search);
        model.addAttribute("currentPage", page);
        model.addAttribute("activePage", "Home");
        return "home/index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("activePage", "About");
        return "home/about";
    }

    @GetMapping("/terms")
    public String terms() {
        return "home/terms";
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "home/privacy";
    }

    @GetMapping("/cookies")
    public String cookies() {
        return "home/cookies";
    }

    @GetMapping("/disclaimer")
    public String disclaimer() {
        return "home/disclaimer";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "home/access-denied";
    }
}