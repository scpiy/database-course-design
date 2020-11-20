package com.scpi.clothes.controller;

import com.scpi.clothes.model.AssociationOfClothAndOrder;
import com.scpi.clothes.model.ClothesOrder;
import com.scpi.clothes.model.Orders;
import com.scpi.clothes.model.User;
import com.scpi.clothes.repository.AssociationOfClothAndOrderRepository;
import com.scpi.clothes.repository.ClothesOrderRepository;
import com.scpi.clothes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/user")
@Controller
public class UserAccountController {
    UserRepository userRepository;
    ClothesOrderRepository clothesOrderRepository;
    AssociationOfClothAndOrderRepository associationOfClothAndOrderRepository;

    public UserAccountController(UserRepository userRepository, ClothesOrderRepository clothesOrderRepository, AssociationOfClothAndOrderRepository associationOfClothAndOrderRepository) {
        this.userRepository = userRepository;
        this.clothesOrderRepository = clothesOrderRepository;
        this.associationOfClothAndOrderRepository = associationOfClothAndOrderRepository;
    }

    @GetMapping("information")
    public String welcomePage(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "/user/information";
    }

    @GetMapping("/account")
    public String showUserAccount(@AuthenticationPrincipal User user, Model model) {
        List<ClothesOrder> clothesOrders = clothesOrderRepository.findAllByOwnUserOrderByCreateAtDesc(user);
        model.addAttribute("orders", clothesOrders);
        return "/user/orders";
    }

    @GetMapping("/deleteOrder")
    public String deleteOrder(@RequestParam Long orderId) {
        clothesOrderRepository.deleteById(orderId);
        return "redirect:/mall";
    }
}
