package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.entity.Role;
import com.aimanecouissi.quizard.entity.User;
import com.aimanecouissi.quizard.enums.Provider;
import com.aimanecouissi.quizard.repository.RoleRepository;
import com.aimanecouissi.quizard.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserAuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void oAuth2Signup(String email, String name, Provider provider) {
        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setFirstName(name);
            user.setEmail(email);
            Role userRole = roleRepository.findByName("ROLE_USER");
            user.setRoles(Collections.singletonList(userRole));
            user.setProvider(provider);
            userRepository.save(user);
        } else {
            User user = userRepository.findByEmail(email);
            user.setProvider(provider);
            userRepository.save(user);
        }
    }
}