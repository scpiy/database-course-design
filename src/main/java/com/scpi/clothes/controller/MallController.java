package com.scpi.clothes.controller;

import com.scpi.clothes.model.*;
import com.scpi.clothes.repository.AssociationOfClothAndOrderRepository;
import com.scpi.clothes.repository.ClothesOrderRepository;
import com.scpi.clothes.repository.ClothesRepository;
import com.scpi.clothes.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/mall")
@SessionAttributes("orderList")
public class MallController {
    UserRepository userRepository;
    ClothesRepository clothesRepository;
    ClothesOrderRepository clothesOrderRepository;
    AssociationOfClothAndOrderRepository associationOfClothAndOrderRepository;

    public MallController(ClothesRepository clothesRepository, UserRepository userRepository, ClothesOrderRepository clothesOrderRepository, AssociationOfClothAndOrderRepository associationOfClothAndOrderRepository) {
        this.clothesRepository = clothesRepository;
        this.userRepository = userRepository;
        this.clothesOrderRepository = clothesOrderRepository;
        this.associationOfClothAndOrderRepository = associationOfClothAndOrderRepository;
    }

    @ModelAttribute(name = "orderList")
    public List<Orders> cart() {
        return new ArrayList<>();
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
    public String addToCart(@PathVariable("id") Long id, @ModelAttribute(name = "orderList") List<Orders> ordersList, Integer number) {
        Clothes cloth = clothesRepository.getOne(id);
        ordersList.add(new Orders(cloth, number));
        return "redirect:/mall";
    }

    @GetMapping("payment")
    public String payment(@AuthenticationPrincipal User user, @ModelAttribute(name = "orderList") List<Orders> ordersList, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("orderList", ordersList);
        long totalCost = 0L;
        for (var order : ordersList) {
            totalCost += order.getClothes().getPrice() * order.getCountOfCloth();
        }
        model.addAttribute("totalCost", totalCost);
        return "/mall/payment";
    }

    @PostMapping("payment")
    public String processPayment(@ModelAttribute(name = "orderList") List<Orders> ordersList, @AuthenticationPrincipal User user, SessionStatus sessionStatus) {
        /*
            TODO I should add a check box make user select some clothes to buy, just like hxf suggest.
         */
        long amount = 0L;
        ClothesOrder clothesOrder = new ClothesOrder();
        clothesOrder.setPayment(false);
        clothesOrder.setOwnUser(user);
        for (var order : ordersList)
            amount += order.getClothes().getPrice() * order.getCountOfCloth();
        clothesOrder.setAmount(amount);
        clothesOrderRepository.save(clothesOrder);
        for (var order : ordersList) {
            AssociationOfClothAndOrder associationOfClothAndOrder = new AssociationOfClothAndOrder();
            associationOfClothAndOrder.setCloth(order.getClothes());
            associationOfClothAndOrder.setOrder(clothesOrder);
            associationOfClothAndOrder.setCountOfCloth(order.getCountOfCloth());
            associationOfClothAndOrderRepository.save(associationOfClothAndOrder);
        }
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
