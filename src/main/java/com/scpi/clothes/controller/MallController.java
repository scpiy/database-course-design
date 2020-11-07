package com.scpi.clothes.controller;

import com.scpi.clothes.model.Clothes;
import com.scpi.clothes.model.ClothesOrder;
import com.scpi.clothes.model.User;
import com.scpi.clothes.repository.ClothesOrderRepository;
import com.scpi.clothes.repository.ClothesRepository;
import com.scpi.clothes.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mall")
@SessionAttributes("cart")
public class MallController {
    UserRepository userRepository;
    ClothesRepository clothesRepository;
    ClothesOrderRepository clothesOrderRepository;

    public MallController(ClothesRepository clothesRepository, UserRepository userRepository, ClothesOrderRepository clothesOrderRepository) {
        this.clothesRepository = clothesRepository;
        this.userRepository = userRepository;
        this.clothesOrderRepository = clothesOrderRepository;
    }

    @ModelAttribute(name = "cart")
    public ClothesOrder cart(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        ClothesOrder cart = new ClothesOrder();
        cart.setUserId(user.getId());
        cart.setPayment(false);
        cart.setClothes(new ArrayList<>());
        return cart;
    }

    @GetMapping
    public String showClothes(Model model, Principal Principal) {
        User user = userRepository.findByUsername(Principal.getName());
        model.addAttribute("user", user);
        List<Clothes> clothes = clothesRepository.findAll();
        model.addAttribute("clothes", clothes);
        return "/mall/show";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") Long id, Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        Clothes cloth = clothesRepository.getOne(id);
        model.addAttribute("cloth", cloth);
        return "/mall/detail";
    }

    @PostMapping("/{id}")
    public String addToCart(@PathVariable("id") Long id, @ModelAttribute ClothesOrder cart) {
        Clothes cloth = clothesRepository.getOne(id);
        cart.getClothes().add(cloth);
        return "redirect:/mall";
    }

    @GetMapping("payment")
    public String payment(Principal principal, @ModelAttribute ClothesOrder cart, Model model) {
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("cart", cart);
        return "/mall/payment";
    }

    @PostMapping("payment")
    public String processPayment(@ModelAttribute ClothesOrder cart, Principal principal) {
        cart.setPayment(true);
        clothesOrderRepository.save(cart);
        cart = new ClothesOrder();
        cart.setUserId(userRepository.findByUsername(principal.getName()).getId());
        cart.setPayment(false);
        cart.setClothes(new ArrayList<>());
        return "redirect:/mall";
    }
}
