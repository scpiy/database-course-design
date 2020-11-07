package com.scpi.clothes;

import com.scpi.clothes.model.Clothes;
import com.scpi.clothes.model.ClothesOrder;
import com.scpi.clothes.repository.SupplierRepository;
import com.scpi.clothes.repository.ClothesOrderRepository;
import com.scpi.clothes.repository.ClothesRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class ClothesApplicationTests {
    @Autowired
    ClothesRepository clothesRepository;

    @Autowired
    ClothesOrderRepository clothesOrderRepository;

    @Autowired
    SupplierRepository supplierRepository;

    @Test
    void clothesTest() {
        Clothes clothes = new Clothes();
        clothes.setInventory(200L);
        clothesRepository.save(clothes);
        Assert.assertEquals(200L, (long) clothesRepository.getOne(1L).getInventory());
    }

    @Test
    void clothesOrderTest() {
        List<Clothes> clothes = new ArrayList<>(5);
        for (int i = 0; i < 5; ++i) {
            clothes.add(new Clothes());
            clothes.get(i).setName("xxz" + i);
        }
        for (int i = 0; i < 5; ++i)
            clothesRepository.save(clothes.get(i));
        List<Clothes> clothesInOrder = new ArrayList<>(2);
        clothesInOrder.add(clothes.get(1));
        clothesInOrder.add(clothes.get(4));
        ClothesOrder clothesOrder = new ClothesOrder();
        clothesOrder.setClothes(clothesInOrder);
        clothesOrderRepository.save(clothesOrder);
        Assert.assertEquals(clothesOrderRepository.getOne(6L).getClothes().size(), 2);
    }

    @Test
    void contextLoads() {
    }

}
