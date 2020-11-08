package com.scpi.clothes.controller;

import com.scpi.clothes.model.Clothes;
import com.scpi.clothes.model.User;
import com.scpi.clothes.repository.ClothesOrderRepository;
import com.scpi.clothes.repository.ClothesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Controller
@RequestMapping("/faker")
@Slf4j
public class FakerController {
    ClothesRepository clothesRepository;
    ClothesOrderRepository clothesOrderRepository;

    public FakerController(ClothesRepository clothesRepository, ClothesOrderRepository clothesOrderRepository) {
        this.clothesRepository = clothesRepository;
        this.clothesOrderRepository = clothesOrderRepository;
    }


//    @GetMapping
    public String generateAll() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/scpi/user"))) {
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                String[] fields = str.split(" ");
                Clothes clothes = new Clothes();
                clothes.setName(fields[0]);
                clothes.setInventory(Long.valueOf(fields[1]));
                clothes.setTotalSales(Long.valueOf(fields[2]));
                clothes.setType(fields[3]);
                clothes.setAudienceType(fields[4]);
//                clothesRepository.save(clothes);
                log.info("success persistence the cloth " + clothes.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "login";
    }

//    @GetMapping("clothes")
    public String generateClothes() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/scpi/clothes"))) {
            String cloth;
            while ((cloth = bufferedReader.readLine()) != null) {
                String[] fields = cloth.split(" ");
                Clothes clothes = new Clothes();
                clothes.setName(fields[0]);
                clothes.setInventory(Long.valueOf(fields[1]));
                clothes.setTotalSales(Long.valueOf(fields[2]));
                clothes.setType(fields[3]);
                clothes.setAudienceType(fields[4]);
                clothes.setSpringSales(Long.valueOf(fields[5]));
                clothes.setSummerSales(Long.valueOf(fields[6]));
                clothes.setAutumnSales(Long.valueOf(fields[7]));
                clothes.setWinterSales(Long.valueOf(fields[8]));
                clothesRepository.save(clothes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "login";
    }
}
