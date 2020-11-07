package com.scpi.clothes.controller;

import com.scpi.clothes.form.RegisterForm;
import com.scpi.clothes.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/register")
@Controller
public class RegistrationController {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    public RegistrationController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String registerForm() {
        return "register";
    }

    @PostMapping
    public String processRegister(RegisterForm registerForm) {
        userRepository.save(registerForm.toUser(passwordEncoder));
        return "redirect:/login";
    }
}
