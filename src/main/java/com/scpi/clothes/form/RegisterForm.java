package com.scpi.clothes.form;

import com.scpi.clothes.model.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class RegisterForm {
    private String username;
    private String password;
    private String address;
    private String phoneNumber;

    public User toUser(PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        return user;
    }
}
