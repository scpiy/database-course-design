package com.scpi.clothes.controller;

import com.scpi.clothes.form.AdminRegisterForm;
import com.scpi.clothes.form.RegisterForm;
import com.scpi.clothes.repository.AdminRepository;
import com.scpi.clothes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    AdminRepository adminRepository;

    public RegistrationController(PasswordEncoder passwordEncoder, UserRepository userRepository, AdminRepository adminRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
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

    @GetMapping("/admin")
    public String adminRegisterForm() {
        return "/dist/register";
    }

    @PostMapping("/admin")
    public String processAdminRegister(AdminRegisterForm adminRegisterForm) {
        adminRepository.save(adminRegisterForm.toAdmin(passwordEncoder));
        return "redirect:/login";
    }
}
