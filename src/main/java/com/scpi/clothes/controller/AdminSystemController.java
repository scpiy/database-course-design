package com.scpi.clothes.controller;

import com.scpi.clothes.form.AddClothForm;
import com.scpi.clothes.form.ChangeClothForm;
import com.scpi.clothes.form.SearchClothForm;
import com.scpi.clothes.model.Admin;
import com.scpi.clothes.model.Clothes;
import com.scpi.clothes.model.ClothesOrder;
import com.scpi.clothes.model.User;
import com.scpi.clothes.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminSystemController {
    AdminRepository adminRepository;
    ClothesRepository clothesRepository;
    ClothesOrderRepository clothesOrderRepository;

    public AdminSystemController(ClothesRepository clothesRepository, ClothesOrderRepository clothesOrderRepository, AdminRepository adminRepository) {
        this.clothesRepository = clothesRepository;
        this.clothesOrderRepository = clothesOrderRepository;
        this.adminRepository = adminRepository;
    }

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal Admin user) {
        List<Long> sales = new ArrayList<>(4);
        for (int i = 0; i <4; ++i)
            sales.add(0L);
        List<Clothes> clothes = clothesRepository.findAll();
        Long totalSales = 0L;
        for (var cloth : clothes) {
            sales.set(0, sales.get(0) + cloth.getSpringSales());
            sales.set(1, sales.get(1) + cloth.getSummerSales());
            sales.set(2, sales.get(2) + cloth.getAutumnSales());
            sales.set(3, sales.get(3) + cloth.getWinterSales());
            totalSales += cloth.getTotalSales();
        }
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("sales", sales);
        model.addAttribute("clothes", clothes);
        model.addAttribute("user", user);

        return "dist/index";
    }

    @GetMapping("/{pageType}")
    public String card(Model model, @PathVariable("pageType") String pageType, @AuthenticationPrincipal Admin user) {
        List<Long> sales = new ArrayList<>(4);
        for (int i = 0; i <4; ++i)
            sales.add(0L);
        List<Clothes> clothes;
        Long totalSales = 0L;
        if (pageType == null)
            pageType = "AllClothes";
        List<ClothesOrder> orders;
        model.addAttribute("user", user);
        if ("AllClothes".equals(pageType) || "PopularClothes".equals(pageType)) {
            if ("AllClothes".equals(pageType))
                clothes = clothesRepository.findAll();
            else {
                PageRequest pageRequest = PageRequest.of(0, 15, Sort.by("totalSales").descending());
                clothes = clothesRepository.findAll(pageRequest).getContent();
            }
            for (var cloth : clothes) {
                sales.set(0, sales.get(0) + cloth.getSpringSales());
                totalSales += cloth.getSpringSales();
                sales.set(1, sales.get(1) + cloth.getSummerSales());
                totalSales += cloth.getSummerSales();
                sales.set(2, sales.get(2) + cloth.getAutumnSales());
                totalSales += cloth.getAutumnSales();
                sales.set(3, sales.get(3) + cloth.getWinterSales());
                totalSales += cloth.getWinterSales();
            }
            model.addAttribute("clothes", clothes);
            model.addAttribute("sales", sales);
            model.addAttribute("totalSales", totalSales);
            return "dist/index";
        } else {
            if ("AllOrders".equals(pageType) || "UnpaidOrder".equals(pageType))
                orders = clothesOrderRepository.findAll();
            else
                orders = clothesOrderRepository.findAll().stream()
                        .filter(order -> !order.isPayment())
                        .collect(Collectors.toList());
            model.addAttribute("orders", orders);
            model.addAttribute("sales", sales);
            model.addAttribute("totalSales", totalSales);
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
    public String addCloth(Model model, @AuthenticationPrincipal Admin user) {
        model.addAttribute("user", user);
        return "dist/addCloth";
    }

    @PostMapping("/addCloth")
    public String processAddCloth(AddClothForm clothForm) {
        clothesRepository.save(clothForm.toCloth());
        return "redirect:/admin";
    }

    @GetMapping("/changeCloth")
    public String changeCloth(Model model, @AuthenticationPrincipal Admin user,
                              @RequestParam(value = "clothId", required = false) Long clothId,
                              @RequestParam(value = "clothName", required = false) String clothName) {
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
    public String deleteCloth(@RequestParam(value = "clothId") Long clothId, Model model, @AuthenticationPrincipal Admin user) {
        model.addAttribute("user", user);
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
