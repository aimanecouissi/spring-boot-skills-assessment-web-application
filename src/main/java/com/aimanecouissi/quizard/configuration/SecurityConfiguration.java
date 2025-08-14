package com.aimanecouissi.quizard.configuration;

import com.aimanecouissi.quizard.handler.OAuth2LoginSuccessHandler;
import com.aimanecouissi.quizard.oauth2.ApplicationOAuth2UserService;
import com.aimanecouissi.quizard.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final ApplicationOAuth2UserService applicationOAuth2UserService;
    private final UserDetailsService userDetailsService;

    public SecurityConfiguration(OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler, ApplicationOAuth2UserService applicationOAuth2UserService, UserDetailsService userDetailsService) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.applicationOAuth2UserService = applicationOAuth2UserService;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/about", "/terms", "/privacy", "/cookies", "/disclaimer", "/css/**", "/images/**", "/js/**", "/upload/**").permitAll()
                        .requestMatchers("/signup", "/signin", "/password/forgot", "/password/reset", "/oauth2/**").anonymous()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/signin")
                        .loginProcessingUrl("/signin")
                        .usernameParameter("email")
                        .permitAll())
                .oauth2Login(oauth -> oauth
                        .loginPage("/signin")
                        .userInfoEndpoint(user -> user
                                .userService(applicationOAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler))
                .logout(logout -> logout
                        .logoutSuccessUrl("/signin").permitAll())
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied"))
                .rememberMe(remember -> remember.userDetailsService(userDetailsService))
                .build();
    }
}