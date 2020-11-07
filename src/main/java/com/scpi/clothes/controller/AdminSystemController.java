package com.scpi.clothes.controller;

import com.scpi.clothes.model.Clothes;
import com.scpi.clothes.model.User;
import com.scpi.clothes.repository.ClothesOrderRepository;
import com.scpi.clothes.repository.ClothesRepository;
import com.scpi.clothes.repository.SupplierRepository;
import com.scpi.clothes.repository.UserRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.stylesheets.LinkStyle;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminSystemController {
    UserRepository userRepository;
    ClothesRepository clothesRepository;
    ClothesOrderRepository clothesOrderRepository;
    SupplierRepository supplierRepository;

    public AdminSystemController(ClothesRepository clothesRepository, ClothesOrderRepository clothesOrderRepository, SupplierRepository supplierRepository, UserRepository userRepository) {
        this.clothesRepository = clothesRepository;
        this.clothesOrderRepository = clothesOrderRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String index(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        List<Clothes> clothes = clothesRepository.findAll();
        model.addAttribute("clothes", clothes);
        model.addAttribute("user", user);
        return "dist/index";
    }

    @GetMapping("/chars")
    public String chars(Model model) {
        return "dist/charts";
    }

    @GetMapping("/tables")
    public String tables(Model model) {
        return "dist/tables";
    }
}

//        for (int i = 0; i < 5; ++i) {
//            Clothes clothes = new Clothes();
//            clothes.setName("xxz" + i);
//            clothes.setInventory((long) (40 + 10 * i));
//            clothes.setTotalSales((long) (200 + 10 * i));
//            clothes.setAudienceType("audienceType" + i);
//            clothes.setType("type" + i);
//            clothesRepository.save(clothes);
//        }
