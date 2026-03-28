package com.aimanecouissi.quizard.seeder;

import com.aimanecouissi.quizard.entity.Role;
import com.aimanecouissi.quizard.entity.User;
import com.aimanecouissi.quizard.enums.Provider;
import com.aimanecouissi.quizard.service.RoleService;
import com.aimanecouissi.quizard.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!dataExists()) {
            Role adminRole = new Role(null, "ROLE_ADMIN", null);
            Role userRole = new Role(null, "ROLE_USER", null);
            User adminUser = new User();
            User user = new User();
            adminUser.setFirstName("John");
            adminUser.setLastName("Doe");
            adminUser.setEmail("admin@quizard.com");
            adminUser.setPassword(passwordEncoder.encode("0"));
            adminUser.setRoles(List.of(adminRole));
            adminUser.setProvider(Provider.QUIZARD);
            user.setFirstName("Jane");
            user.setLastName("Doe");
            user.setEmail("user@quizard.com");
            user.setPassword(passwordEncoder.encode("0"));
            user.setRoles(List.of(userRole));
            user.setProvider(Provider.QUIZARD);
            roleService.saveAll(List.of(adminRole, userRole));
            userService.saveAll(List.of(adminUser, user));
        }
    }

    private boolean dataExists() {
        List<User> users = userService.findAll();
        List<Role> roles = roleService.findAll();
        return !roles.isEmpty() || !users.isEmpty();
    }
}