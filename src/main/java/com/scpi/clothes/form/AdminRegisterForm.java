package com.scpi.clothes.form;

import com.scpi.clothes.model.Admin;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class AdminRegisterForm {
    String username;
    String password;

    public Admin toAdmin(PasswordEncoder passwordEncoder) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        return admin;
    }
}
