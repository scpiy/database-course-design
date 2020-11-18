package com.scpi.clothes.controller;

import com.scpi.clothes.model.Clothes;
import com.scpi.clothes.model.ClothesOrder;
import com.scpi.clothes.model.User;
import com.scpi.clothes.repository.ClothesOrderRepository;
import com.scpi.clothes.repository.ClothesRepository;
import com.scpi.clothes.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

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
    public ClothesOrder cart(@AuthenticationPrincipal User user) {
        ClothesOrder cart = new ClothesOrder();
        cart.setOwnUser(user);
        cart.setPayment(false);
        cart.setClothes(new ArrayList<>());
        cart.setAmount(0L);
        return cart;
    }

    @GetMapping
    public String showClothes(Model model, @AuthenticationPrincipal User user, @RequestParam(name = "audienceType", required = false) String audienceType) {
        model.addAttribute("user", user);
        List<String> audiences = clothesRepository.findAllAudience();
        model.addAttribute("audiences", audiences);
        List<Clothes> clothes;
        if (audienceType == null)
            clothes = clothesRepository.findAll();
        else
            clothes = clothesRepository.findAllByAudienceType(audienceType);
        model.addAttribute("clothes", clothes);
        return "/mall/index";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        Clothes cloth = clothesRepository.getOne(id);
        model.addAttribute("cloth", cloth);
        return "/mall/detail";
    }

    @PostMapping("/{id}")
    public String addToCart(@PathVariable("id") Long id, @ModelAttribute(name = "cart") ClothesOrder cart, Integer number) {
        Clothes cloth = clothesRepository.getOne(id);
        for (int i = 0; i < number; ++i)
            cart.getClothes().add(cloth);
        return "redirect:/mall";
    }

    @GetMapping("payment")
    public String payment(@AuthenticationPrincipal User user, @ModelAttribute(name = "cart") ClothesOrder cart, Model model) {
        model.addAttribute("user", user);
        // TODO create a new model to store the item and the number of the item in the cart
        model.addAttribute("cart", cart);
        Long totalCost = 0L;
        for (var cloth : cart.getClothes())
            totalCost += cloth.getPrice();
        model.addAttribute("totalCost", totalCost);
        return "/mall/payment";
    }

    @PostMapping("payment")
    public String processPayment(@ModelAttribute(name = "cart") ClothesOrder cart, @AuthenticationPrincipal User user, SessionStatus sessionStatus) {
        /*
            TODO I should add a check box make user select some clothes to buy, just like hxf suggest.
         */
        Long amount = 0L;
        for (var cloth : cart.getClothes()) {
            amount += cloth.getPrice();
        }
        cart.setAmount(amount);
        clothesOrderRepository.save(cart);
        sessionStatus.setComplete();
        return "redirect:/mall";
    }

    @GetMapping("pay")
    public String processPay(@RequestParam Long orderId) {
        ClothesOrder order = clothesOrderRepository.getOne(orderId);
        order.setPayment(true);
        clothesOrderRepository.save(order);
        return "redirect:/user/account";
    }
}
