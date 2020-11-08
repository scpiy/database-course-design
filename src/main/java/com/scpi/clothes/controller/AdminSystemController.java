package com.scpi.clothes.controller;

import com.scpi.clothes.form.AddClothForm;
import com.scpi.clothes.form.ChangeClothForm;
import com.scpi.clothes.form.SearchClothForm;
import com.scpi.clothes.model.Clothes;
import com.scpi.clothes.model.ClothesOrder;
import com.scpi.clothes.model.User;
import com.scpi.clothes.repository.ClothesOrderRepository;
import com.scpi.clothes.repository.ClothesRepository;
import com.scpi.clothes.repository.SupplierRepository;
import com.scpi.clothes.repository.UserRepository;
import com.sun.xml.bind.v2.TODO;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<Integer> sales = new ArrayList<>();
        List<Clothes> clothes = clothesRepository.findAll();
        User user = userRepository.findByUsername(principal.getName());
        for (int i = 0; i < 13; ++i) {
            sales.add(10000 + 2000 * i);
        }
        model.addAttribute("sales", sales);
        model.addAttribute("clothes", clothes);
        model.addAttribute("user", user);

        return "dist/index";
    }

    @GetMapping("/{pageType}")
    public String card(Model model, Principal principal, @PathVariable("pageType") String pageType) {
        if (pageType == null)
            pageType = "AllClothes";
        List<Integer> sales = new ArrayList<>();
        List<Clothes> clothes = new ArrayList<>();
        List<ClothesOrder> orders = new ArrayList<>();
        User user = userRepository.findByUsername(principal.getName());
        for (int i = 0; i < 13; ++i) {
            sales.add(10000 + 2000 * i);
        }
        model.addAttribute("sales", sales);
        model.addAttribute("user", user);
        if ("AllClothes".equals(pageType) || "PopularClothes".equals(pageType)) {
            if ("AllClothes".equals(pageType))
                clothes = clothesRepository.findAll();
            else
                clothes = clothesRepository.findAll().stream()
                        .sorted((a, b) -> (int) (a.getTotalSales() - b.getTotalSales()))
                        .limit(10).collect(Collectors.toList());
            model.addAttribute("clothes", clothes);
            return "dist/index";
        } else {
            if ("AllOrders".equals(pageType) || "UnpaidOrder".equals(pageType))
                orders = clothesOrderRepository.findAll();
            else
                orders = clothesOrderRepository.findAll().stream()
                        .filter(order -> !order.isPayment())
                        .collect(Collectors.toList());
            model.addAttribute("orders", orders);
            return "dist/order";
        }
    }

    @GetMapping("/charts")
    public String chars(Model model) {
        return "dist/charts";
    }

    @GetMapping("/tables")
    public String tables(Model model) {
        return "dist/tables";
    }

    @GetMapping("/addCloth")
    public String addCloth(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "dist/addCloth";
    }

    @PostMapping("/addCloth")
    public String processAddCloth(AddClothForm clothForm) {
        clothesRepository.save(clothForm.toCloth());
        return "redirect:/admin";
    }

    @GetMapping("/changeCloth")
    public String changeCloth(Model model, Principal principal,
                              @RequestParam(value = "clothId", required = false) Long clothId,
                              @RequestParam(value = "clothName", required = false) String clothName) {
        User user = userRepository.findByUsername(principal.getName());
        Clothes clothes = new Clothes();
        model.addAttribute("user", user);
        boolean getClothById = false;
        boolean getClothByName = false;
        // TODO make change page's find by name like the index page
        if (clothId != null) {
            getClothById = true;
            clothes = clothesRepository.findById(clothId).get();
        }
        model.addAttribute("clothes", clothes);
        model.addAttribute("getClothById", getClothById);
        return "dist/changeCloth";
    }

    @PostMapping("/changeCloth")
    @Transactional
    public String processChangeCloth(ChangeClothForm changeClothForm) {
        Clothes clothes = changeClothForm.toCloth();
        clothesRepository.update(clothes.getName(), clothes.getType(), clothes.getAudienceType(), clothes.getInventory(), clothes.getId());
        return "redirect:/admin/changeCloth";
    }

    @PostMapping("/searchCloth")
    public String searchCloth(SearchClothForm searchClothForm) {
        Optional<Clothes> clothes = clothesRepository.findById(searchClothForm.getClothId());
        Clothes cloth = clothes.get();
        return "redirect:/admin/changeCloth?clothId=" + cloth.getId();
    }

    @GetMapping("/delete")
    public String deleteCloth(@RequestParam(value = "clothId") Long clothId, Model model, Principal principal) {
        model.addAttribute("user", userRepository.findByUsername(principal.getName()));
        model.addAttribute("clothes", clothesRepository.findById(clothId).get());
        return "dist/deleteCloth";
    }

    @PostMapping("/delete")
    @Transactional
    public String processDeleteCloth(ChangeClothForm clothForm) {
        clothesRepository.deleteById(clothForm.getClothId());
        return "redirect:/admin";
    }
}
