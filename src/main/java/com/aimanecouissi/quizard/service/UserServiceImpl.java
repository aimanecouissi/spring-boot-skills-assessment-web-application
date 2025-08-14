package com.aimanecouissi.quizard.service;

import com.aimanecouissi.quizard.dto.UserDto;
import com.aimanecouissi.quizard.entity.Role;
import com.aimanecouissi.quizard.entity.User;
import com.aimanecouissi.quizard.enums.Provider;
import com.aimanecouissi.quizard.oauth2.ApplicationOAuth2User;
import com.aimanecouissi.quizard.repository.RoleRepository;
import com.aimanecouissi.quizard.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public String findEmailByAuthentication(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        return (principal instanceof User) ? ((User) principal).getEmail() : ((ApplicationOAuth2User) principal).getEmail();
    }

    @Override
    public User findByPasswordResetToken(String token) {
        return userRepository.findByPasswordResetToken(token);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    @Override
    public void create(UserDto userDto) {
        Role userRole = roleRepository.findByName("ROLE_USER");
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(Collections.singletonList(userRole));
        user.setProvider(Provider.QUIZARD);
        userRepository.save(user);
    }

    @Override
    public void updateName(String email, String firstName, String lastName) {
        User user = userRepository.findByEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null);
        userRepository.save(user);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updatePasswordResetToken(String email, String token) {
        if (existsByEmail(email)) {
            User user = userRepository.findByEmail(email);
            user.setPasswordResetToken(token);
            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return user;
    }
}