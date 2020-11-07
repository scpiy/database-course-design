package com.scpi.clothes.controller;

import com.scpi.clothes.model.ClothesOrder;
import com.scpi.clothes.model.User;
import com.scpi.clothes.repository.ClothesOrderRepository;
import com.scpi.clothes.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;


@RequestMapping("/user")
@Controller
public class UserAccountController {
    UserRepository userRepository;
    ClothesOrderRepository clothesOrderRepository;

    public UserAccountController(UserRepository userRepository, ClothesOrderRepository clothesOrderRepository) {
        this.userRepository = userRepository;
        this.clothesOrderRepository = clothesOrderRepository;
    }

    @GetMapping("information")
    public String welcomePage(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "/user/information";
    }

    @GetMapping("/order")
    public String showUserAccount(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        List<ClothesOrder> orders = clothesOrderRepository.findAllByUserId(user.getId());
        model.addAttribute("orders", orders);
        return "/user/orders";
    }
}
