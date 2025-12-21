package com.hahn.tasks.task_manager_backend.auth;

import com.hahn.tasks.task_manager_backend.user.User;
import com.hahn.tasks.task_manager_backend.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        createDefaultUserIfNotExists();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(user -> {
                    // إنشاء token جديد وتخزينه في DB
                    String token = UUID.randomUUID().toString();
                    user.setToken(token);
                    userRepository.save(user);

                    LoginResponse response =
                            new LoginResponse(token, user.getEmail(), user.getFullName());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private void createDefaultUserIfNotExists() {
        String email = "test@hahn.com";
        String rawPassword = "password123";

        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User(
                    email,
                    passwordEncoder.encode(rawPassword),
                    "Default User"
            );
            userRepository.save(user);
        }
    }
}
