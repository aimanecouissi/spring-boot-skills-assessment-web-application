package com.aimanecouissi.quizard.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry resourceHandlerRegistry) {
        exposeDirectory(resourceHandlerRegistry);
    }

    private void exposeDirectory(ResourceHandlerRegistry resourceHandlerRegistry) {
        Path uploadDirectory = Paths.get("upload");
        String uploadPath = uploadDirectory.toFile().getAbsolutePath();
        resourceHandlerRegistry.addResourceHandler("/upload/**").addResourceLocations("file:/" + uploadPath + "/");
    }
}